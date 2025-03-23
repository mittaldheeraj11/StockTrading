package entity;

import java.time.LocalDateTime;

public class Trade {

    private String tradeId;

    private String sellOrderId;

    private String buyOrderId;

    private int quantity;

    private double perShareAmount;

    private LocalDateTime tradeTime;

    private String stockId;

    public Trade(String tradeId, String sellOrderId, String buyOrderId, int quantity, double perShareAmount, LocalDateTime tradeTime, String stockId) {
        this.tradeId = tradeId;
        this.sellOrderId = sellOrderId;
        this.buyOrderId = buyOrderId;
        this.quantity = quantity;
        this.perShareAmount = perShareAmount;
        this.tradeTime = tradeTime;
        this.stockId = stockId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Trade details : ").append("Buyer Order-Id ").append(buyOrderId). append(" Seller Order-Id ").append(sellOrderId)
                .append(" Quantity ").append(quantity).append(" per share amount ").append(perShareAmount);
        return sb.toString();
    }
}
