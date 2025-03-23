package entity;

import enums.OrderType;

import java.util.Map;

public class TradeResponse {
    private OrderType orderType;

    private Map<Integer, Double> transactions;

    private int leftQuantity;

    public TradeResponse(OrderType orderType, Map<Integer, Double> transactions, int leftQuantity) {
        this.orderType = orderType;
        this.transactions = transactions;
        this.leftQuantity = leftQuantity;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(orderType).append(" ").append("left quantity :" ).append(leftQuantity).append(" breakup ");
        for (Map.Entry<Integer, Double> entry : transactions.entrySet()) {
            stringBuilder.append("Quantity : " + entry.getKey() + " price " + entry.getValue()).append(" ");
        }
        return stringBuilder.toString();

    }
}
