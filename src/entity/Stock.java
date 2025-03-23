package entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stock {

    private String name;


    private List<Order> buyOrders;

    private List<Order> sellOrders;

    public Stock(String name) {
        this.name = name;
        this.buyOrders = new ArrayList<>();
        this.sellOrders = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Order> getBuyOrders() {
        return buyOrders;
    }

    public List<Order> getSellOrders() {
        return sellOrders;
    }
}
