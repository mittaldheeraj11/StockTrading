package entity;

import enums.OrderStatus;
import enums.OrderType;

import java.time.LocalDateTime;

public class Order {
    private String orderId;

    private String userId;

    private OrderType orderType;

    private String stock;

    private int quantity;

    private int leftQuantity;

    private double price;

    private LocalDateTime orderPlacedTime;

    private OrderStatus orderStatus;

    public String getOrderId() {
        return orderId;
    }

    public String getUserId() {
        return userId;
    }

    public OrderType getOrderType() {
        return orderType;
    }

    public String getStock() {
        return stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public LocalDateTime getOrderPlacedTime() {
        return orderPlacedTime;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public int getLeftQuantity() {
        return leftQuantity;
    }

    public void setLeftQuantity(int leftQuantity) {
        this.leftQuantity = leftQuantity;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Order(String orderId, String userId, OrderType orderType, String stock, int quantity, double price, LocalDateTime orderPlacedTime) {
        this.orderId = orderId;
        this.userId = userId;
        this.orderType = orderType;
        this.stock = stock;
        this.quantity = quantity;
        this.leftQuantity = quantity;
        this.price = price;
        this.orderPlacedTime = orderPlacedTime;
        this.orderStatus = OrderStatus.ACCEPTED;
    }


}
