package service;

import dao.TradeDao;
import entity.Order;
import entity.Stock;
import entity.Trade;
import exception.InvalidStockException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TradeService implements ITradeService{

    private final IStockService service;

    private final TradeDao tradeDao;

    public TradeService(IStockService service, TradeDao tradeDao) {
        this.service = service;
        this.tradeDao = tradeDao;
    }

    public void executeSellRequest(@NotNull Order sellOrderRequest) {
        System.out.println("Got request to execute sell stock: " + sellOrderRequest.getStock() + " quantity: " + sellOrderRequest.getQuantity()
        + " price: " + sellOrderRequest.getPrice());
        Stock stock = this.service.getStockDetails(sellOrderRequest.getStock());
        if (stock == null) {
            throw new InvalidStockException("Not a valid stock-id/name.");
        }
        List<Order> buyOrder = stock.getBuyOrders();
        int quantityRequired = sellOrderRequest.getQuantity();
        synchronized (stock) {
            Queue<Order> availableSellOrders = new PriorityQueue<>(Comparator.comparing(Order::getOrderPlacedTime));
            List<Order> list = buyOrder.stream()
                    .filter(bo -> bo.getPrice() >= sellOrderRequest.getPrice())
                    .sorted(Comparator.comparing(Order::getOrderPlacedTime)).toList();
            availableSellOrders.addAll(list);

            if (availableSellOrders.isEmpty()) {
                // order can't be placed, add in queue.
                System.out.println("Adding order in pending req " + sellOrderRequest.getOrderId() + " quantity " +
                        sellOrderRequest.getQuantity() + " price " + sellOrderRequest.getPrice());
                stock.getSellOrders().add(sellOrderRequest);

            } else {

                List<Order> executedOrders = new ArrayList<>();
                Map<String, Integer> quantityMap = new HashMap<>();
                quantityRequired = getQuantityLeftAndCreateOrder(availableSellOrders, quantityRequired, quantityMap, executedOrders);


                if (!executedOrders.isEmpty()) {
                    List<Trade> tradeCreatedForSell = service.getTradeCreatedForSell(sellOrderRequest, executedOrders, quantityMap, sellOrderRequest.getPrice());
                    tradeDao.addTrade(tradeCreatedForSell);
                    service.markBuyOrdersAtCompleted(executedOrders, stock, quantityMap);
                }


                if (quantityRequired > 0) {
                    System.out.println("Adding order in pending sell req " + sellOrderRequest.getOrderId() + " quantity " +
                            quantityRequired + " price " + sellOrderRequest.getPrice()) ;
                    sellOrderRequest.setLeftQuantity(quantityRequired);
                    stock.getSellOrders().add(sellOrderRequest);
                }
            }
        }

    }




    public void executeBuyRequest(@NotNull Order buyOrderRequest) {
        System.out.println("Got request to execute buy stock: " + buyOrderRequest.getStock() + " quantity: " + buyOrderRequest.getQuantity()
                + " price: " + buyOrderRequest.getPrice());
        Stock stock = this.service.getStockDetails(buyOrderRequest.getStock());
        if (stock == null) {
            throw new InvalidStockException("Not a valid stock-id/name.");
        }

        List<Order> sellOrders = stock.getSellOrders();

        synchronized (stock) {
            List<Order> list = sellOrders.stream().filter(so -> so.getPrice() <= buyOrderRequest.getPrice())
                    .sorted(Comparator.comparing(Order::getOrderPlacedTime)).toList();
            Queue<Order> availableBuyOrders = new PriorityQueue<>(Comparator.comparing(Order::getOrderPlacedTime));
            availableBuyOrders.addAll(list);
            if (availableBuyOrders.isEmpty()) {
                // order can't be executed, add in queue.
                System.out.println("Adding order in pending buy req " + buyOrderRequest.getOrderId() + " quantity " +
                        buyOrderRequest.getQuantity() + " price " + buyOrderRequest.getPrice());
                stock.getBuyOrders().add(buyOrderRequest);

            } else {

                List<Order> executedOrders = new ArrayList<>();
                Map<String, Integer> quantityMap = new HashMap<>();
                Map<String, Double> priceMap = new HashMap<>();
                int quantityLeft = getQuantityLeftCreateOrders(availableBuyOrders, buyOrderRequest.getQuantity(), priceMap, quantityMap, executedOrders);

                if (!executedOrders.isEmpty()) {
                    List<Trade> tradeForBuy = service.createTradeForBuy(buyOrderRequest, executedOrders, quantityMap, priceMap);
                    tradeDao.addTrade(tradeForBuy);
                    service.markSellOrdersAtCompleted(executedOrders, stock, quantityMap);
                }
                if (quantityLeft > 0) {
                    System.out.println("Adding order in pending buy req " + buyOrderRequest.getOrderId() + " quantity " +
                            quantityLeft + " price " + buyOrderRequest.getPrice());
                    buyOrderRequest.setLeftQuantity(quantityLeft);
                    stock.getBuyOrders().add(buyOrderRequest);

                }
            }
        }

    }

    private int getQuantityLeftCreateOrders(Queue<Order> availableBuyOrders, int quantityRequired, Map<String, Double> priceMap, Map<String, Integer> quantityMap, List<Order> executedOrders) {
        while (!availableBuyOrders.isEmpty() && quantityRequired > 0) {

            Order order = availableBuyOrders.poll();
            double amount = order.getPrice();
            int transQuantity = Integer.min(quantityRequired, order.getLeftQuantity());
            quantityRequired -= transQuantity;
            priceMap.put(order.getOrderId(), amount);
            quantityMap.put(order.getOrderId(), transQuantity);
            executedOrders.add(order);

        }
        return quantityRequired;
    }

    private int getQuantityLeftAndCreateOrder(Queue<Order> availableSellOrders, int quantityRequired, Map<String, Integer> map, List<Order> executedOrders) {
        while (!availableSellOrders.isEmpty() && quantityRequired > 0) {
            Order order = availableSellOrders.poll();
            if (order.getLeftQuantity() <= quantityRequired) {
                map.put(order.getOrderId(), order.getLeftQuantity());
                quantityRequired -= order.getLeftQuantity();
                executedOrders.add(order);
            } else {
                order.setLeftQuantity(order.getLeftQuantity() - quantityRequired);
                map.put(order.getOrderId(), quantityRequired);
                executedOrders.add(order);
            }
        }
        return quantityRequired;
    }
}
