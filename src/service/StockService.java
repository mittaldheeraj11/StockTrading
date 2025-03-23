package service;

import dao.StockDao;
import entity.Order;
import entity.Stock;
import entity.Trade;
import exception.InvalidStockException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class StockService implements IStockService{

    private final StockDao stockDao;

    public StockService(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public Stock getStockDetails(String stockId) {

        return this.stockDao.getStock(stockId);
    }

    public Stock createStock(String stockId) {
        return this.stockDao.createStock(stockId);
    }

    @Override
    public List<Trade> createTradeForBuy(Order buyOrder, List<Order> sellOrders, Map<String, Integer> quantityMap, Map<String, Double> priceMap) {

        List<Trade> trades = new ArrayList<>();
        Random random = new Random();
        System.out.println("Trade happened with following details:");
        for (Order order : sellOrders) {
            Trade trade = new Trade(String.valueOf(random.nextInt()), order.getOrderId(), buyOrder.getOrderId(),
                    quantityMap.get(order.getOrderId()), priceMap.get(order.getOrderId()),
                    buyOrder.getOrderPlacedTime(), buyOrder.getStock());
            trades.add(trade);
            System.out.println(trade);
        }
        return trades;
    }

    @Override
    public void markSellOrdersAtCompleted(List<Order> orders, Stock stock, Map<String, Integer> quantityMap) {
        for (Order order : orders) {
            if (quantityMap.get(order.getOrderId()) == order.getLeftQuantity()) {
                stock.getSellOrders().remove(order);
            } else if (quantityMap.get(order.getOrderId()) < order.getLeftQuantity()) {
                int q = order.getLeftQuantity() - quantityMap.get(order.getOrderId());
                order.setLeftQuantity(q);
            }
        }
    }

    @Override
    public List<Trade> getTradeCreatedForSell(Order sellOrder, List<Order> buyOrder, Map<String, Integer> quantityMap, Double perSharePrice) {

        List<Trade> trades = new ArrayList<>();
        Random random = new Random();
        System.out.println("Trade happened with following details:");
        for (Order order : buyOrder) {
            Trade trade = new Trade(String.valueOf(random.nextInt()), sellOrder.getOrderId(), order.getOrderId(),
                    quantityMap.get(order.getOrderId()), perSharePrice,
                    sellOrder.getOrderPlacedTime(), sellOrder.getStock());
            trades.add(trade);
            System.out.println(trade);
        }
        return trades;
    }



    @Override
    public void markBuyOrdersAtCompleted(List<Order> orders, Stock stock, Map<String, Integer> quantityMap) {
        for (Order order : orders) {
            if (quantityMap.get(order.getOrderId()) == order.getLeftQuantity()) {
                stock.getBuyOrders().remove(order);
            } else if (quantityMap.get(order.getOrderId()) < order.getLeftQuantity()) {
                int q = order.getLeftQuantity() - quantityMap.get(order.getOrderId());
                order.setLeftQuantity(q);
            }
        }
    }

    @Override
    public String getBuyPendingRequests(String stockId) {
        Stock stock = stockDao.getStock(stockId);
        if (stock == null) {
            throw new InvalidStockException("Wrong Stock Name.");
        }
        return "Pending Buy Request for stock: " + stockId + " \n" + fun(stock.getBuyOrders());
        //return String.valueOf(stock.getBuyOrders().size());
    }

    private String fun(List<Order> buyOrders) {
        StringBuilder sb = new StringBuilder();
        for (Order order : buyOrders) {
            sb.append("Order Id: ").append(order.getOrderId()).append(" Quantity: ").append(order.getLeftQuantity())
                    .append(" price: ").append(order.getPrice()).append("\n");
        }

        return sb.toString();
    }

    @Override
    public String getSellPendingRequests(String stockId) {
        Stock stock = stockDao.getStock(stockId);
        if (stock == null) {
            throw new InvalidStockException("Wrong Stock Name.");
        }
        return "Pending Sell Request for stock: " + stockId + " \n" + fun(stock.getSellOrders());

    }
}
