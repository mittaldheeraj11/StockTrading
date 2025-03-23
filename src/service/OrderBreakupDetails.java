package service;

import entity.Order;

import java.util.List;
import java.util.Map;

public class OrderBreakupDetails {
    private final Map<String, Double> priceMap;
    private final Map<String, Integer> quantityMap;
    private final List<Order> executedOrders;
    private final int quantityRemaining;

    public OrderBreakupDetails(Map<String, Double> priceMap, Map<String, Integer> quantityMap, List<Order> executedOrders, int quantityRemaining) {
        this.priceMap = priceMap;
        this.quantityMap = quantityMap;
        this.executedOrders = executedOrders;
        this.quantityRemaining = quantityRemaining;
    }

    public Map<String, Double> getPriceMap() {
        return priceMap;
    }

    public Map<String, Integer> getQuantityMap() {
        return quantityMap;
    }

    public List<Order> getExecutedOrders() {
        return executedOrders;
    }

    public int getQuantityRemaining() {
        return quantityRemaining;
    }
}
