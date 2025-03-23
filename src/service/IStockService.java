package service;

import entity.Order;
import entity.Stock;

import java.util.List;
import java.util.Map;

public interface IStockService {
    Stock getStockDetails(String stockId);

    void updateRemainingQuantityForSellerOrders(List<Order> orders, Stock stock, Map<String, Integer> quantityMap);

    void updateRemainingQuantityForBuyOrders(List<Order> orders, Stock stock, Map<String, Integer> quantityMap);

    String getBuyPendingRequests(String stockId);

    String getSellPendingRequests(String stockId);
}
