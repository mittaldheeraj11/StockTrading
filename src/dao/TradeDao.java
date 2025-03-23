package dao;

import entity.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradeDao {

    private final List<Trade> trades;

    public TradeDao() {
        this.trades = new ArrayList<>();
    }

    public void addTrade(List<Trade> tradeList) {
        this.trades.addAll(tradeList);
    }
}
