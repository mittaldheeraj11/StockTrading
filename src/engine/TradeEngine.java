package engine;

import dao.StockDao;
import dao.TradeDao;
import entity.Order;
import entity.Stock;
import entity.TradeResponse;
import exception.InvalidStockException;
import service.StockService;
import service.TradeService;

public class TradeEngine {

    private final StockService service;

    public TradeService tradeService;


    public TradeEngine(StockDao stockDao, TradeDao tradeDao) {
        this.service = new StockService(stockDao);
        this.tradeService = new TradeService(service, tradeDao);
    }


    public Stock createStock(String stockId) {
        return this.service.createStock(stockId);
    }

    public void executeSellRequest(Order sellOrderRequest) {
        try {
            TradeResponse tradeResponse = tradeService.executeSellRequest(sellOrderRequest);
            //System.out.println(tradeResponse);
        } catch (InvalidStockException e) {
            System.out.println("Wrong Stock input " + sellOrderRequest.getStock());
        }
    }

    public void executeBuyRequest(Order buyOrderRequest) {
        try {
            TradeResponse tradeResponse = tradeService.executeBuyRequest(buyOrderRequest);
            //System.out.println(tradeResponse);
        } catch (InvalidStockException e) {
            System.out.println("Wrong Stock input " + buyOrderRequest.getStock());
        }
    }

    public String getBuyPendingRequests(String stockId) {
        return this.service.getBuyPendingRequests(stockId);
    }

    public String getSellPendingRequests(String stockId) {
        return this.service.getSellPendingRequests(stockId);
    }
}
