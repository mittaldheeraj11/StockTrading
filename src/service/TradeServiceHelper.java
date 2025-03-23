package service;

import entity.Order;
import entity.Trade;
import entity.TradeResponse;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TradeServiceHelper {

    /**
     * Create trade for the buy requests.
     * @param buyOrder buy order request
     * @param orderBreakupDetails Details of old sell orders and mapping of price and quantity of exchange for the orders.
     * @return List of Trades.
     */
    public static List<Trade> createTradeForBuy(Order buyOrder, OrderBreakupDetails orderBreakupDetails) {

        List<Trade> trades = new ArrayList<>();
        Random random = new Random();
        System.out.println("Trade happened with following details:");
        for (Order order : orderBreakupDetails.getExecutedOrders()) {
            Trade trade = new Trade(String.valueOf(random.nextInt()), order.getOrderId(), buyOrder.getOrderId(),
                    orderBreakupDetails.getQuantityMap().get(order.getOrderId()), orderBreakupDetails.getPriceMap().get(order.getOrderId()),
                    buyOrder.getOrderPlacedTime(), buyOrder.getStock());
            trades.add(trade);
            System.out.println(trade);
        }
        return trades;
    }

    /**
     * Create trade for the sell requests.
     * @param sellOrder sell order request
     * @param orderBreakupDetails Details of old buy orders and mapping of price and quantity of exchange for the orders.
     * @return List of Trades.
     */
    public static List<Trade> createTradeForSell(Order sellOrder, OrderBreakupDetails orderBreakupDetails) {

        List<Trade> trades = new ArrayList<>();
        Random random = new Random();
        System.out.println("Trade happened with following details:");
        for (Order order : orderBreakupDetails.getExecutedOrders()) {
            Trade trade = new Trade(String.valueOf(random.nextInt()), sellOrder.getOrderId(), order.getOrderId(),
                    orderBreakupDetails.getQuantityMap().get(order.getOrderId()), orderBreakupDetails.getPriceMap().get(order.getOrderId()),
                    sellOrder.getOrderPlacedTime(), sellOrder.getStock());
            trades.add(trade);
            System.out.println(trade);
        }
        return trades;
    }

    /**
     * Gets lists of available old sell requests and overall count request placed for buying at price @price.
     * @param availableSellOrders lists of available old sell requests
     * @param quantityRequired Overall count requested to buy
     * @return Details of List of old sell Orders with which trade would happen and how much quantity of exchange
     * happened and at what price.
     */
    public static OrderBreakupDetails getQuantityLeftCreateOrdersAndCreateOrdersMatched(Queue<Order> availableSellOrders, int quantityRequired) {
        Map<String, Double> priceMap= new HashMap<>();
        Map<String, Integer> quantityMap = new HashMap<>();
        List<Order> executedOrders = new ArrayList<>();
        while (!availableSellOrders.isEmpty() && quantityRequired > 0) {

            Order order = availableSellOrders.poll();
            double amount = order.getPrice();
            int transQuantity = Integer.min(quantityRequired, order.getLeftQuantity());
            quantityRequired -= transQuantity;
            priceMap.put(order.getOrderId(), amount);
            quantityMap.put(order.getOrderId(), transQuantity);
            executedOrders.add(order);

        }
        OrderBreakupDetails orderBreakupDetails = new OrderBreakupDetails(priceMap, quantityMap, executedOrders, quantityRequired);
        return orderBreakupDetails;
    }

    /**
     * Gets lists of available buy requests and overall count request placed for selling at price @price.
     * @param availableBuyOrders List of available buy requests.
     * @param quantityRequired Quantity of stocks requested to buy
     * @param price Buyer price requests.
     * @return Details of List of old Orders with which trade would happen and how much quantity of exchange happened
     * at what price per order.
     */
    public static OrderBreakupDetails getQuantityLeftAndCreateOrder(Queue<Order> availableBuyOrders, int quantityRequired, double price) {
        Map<String, Double> priceMap= new HashMap<>();
        Map<String, Integer> quantityMap = new HashMap<>();
        List<Order> executedOrders = new ArrayList<>();
        while (!availableBuyOrders.isEmpty() && quantityRequired > 0) {
            Order order = availableBuyOrders.poll();
            if (order.getLeftQuantity() <= quantityRequired) {
                quantityMap.put(order.getOrderId(), order.getLeftQuantity());
                quantityRequired -= order.getLeftQuantity();
                executedOrders.add(order);
            } else {
                //order.setLeftQuantity(order.getLeftQuantity() - quantityRequired);
                quantityMap.put(order.getOrderId(), quantityRequired);
                executedOrders.add(order);
                quantityRequired = 0;
            }
            priceMap.put(order.getOrderId(), price);
        }
        return new OrderBreakupDetails(priceMap, quantityMap, executedOrders, quantityRequired);
    }

    public static TradeResponse createResponse(OrderBreakupDetails orderBreakupDetails, @NotNull Order buyOrderRequest) {
        Map<Integer, Double> map = new HashMap<>();
        for (Order order : orderBreakupDetails.getExecutedOrders()) {
            Integer quantity = orderBreakupDetails.getQuantityMap().get(order.getOrderId());
            Double price = orderBreakupDetails.getPriceMap().get(order.getOrderId());
            map.put(quantity, price);
        }
        TradeResponse tradeResponse = new TradeResponse(buyOrderRequest.getOrderType(), map, orderBreakupDetails.getQuantityRemaining());
        return tradeResponse;

    }

}
