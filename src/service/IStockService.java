package service;

import entity.Order;
import entity.Stock;
import entity.Trade;

import java.util.List;
import java.util.Map;

public interface IStockService {
    Stock getStockDetails(String stockId);

    List<Trade> createTradeForBuy(Order buyOrder, List<Order> sellOrders, Map<String, Integer> quantityMap, Map<String, Double> priceMap);

    void markSellOrdersAtCompleted(List<Order> orders, Stock stock, Map<String, Integer> quantityMap);

    List<Trade> getTradeCreatedForSell(Order sellOrder, List<Order> buyOrder, Map<String, Integer> quantityMap, Double perSharePrice);

    void markBuyOrdersAtCompleted(List<Order> orders, Stock stock, Map<String, Integer> quantityMap);

    String getBuyPendingRequests(String stockId);

    String getSellPendingRequests(String stockId);
}
