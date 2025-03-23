package dao;

import entity.Stock;

import java.util.HashMap;
import java.util.Map;

public class StockDao {

    private final Map<String, Stock> stockMap;

    public StockDao() {
        this.stockMap = new HashMap<>();
    }

    public Stock getStock(String stockId) {
        return this.stockMap.get(stockId);
    }

    public Stock createStock(String stockId) {
        Stock stock = new Stock(stockId);
        this.stockMap.put(stockId, stock);
        return stock;

    }
}
