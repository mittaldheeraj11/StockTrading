import dao.StockDao;
import dao.TradeDao;
import engine.TradeEngine;
import entity.Order;
import enums.OrderType;
import entity.Stock;

import java.time.LocalDateTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) throws InterruptedException {

        StockDao stockDao = new StockDao();

        TradeDao tradeDao = new TradeDao();
        TradeEngine tradeEngine = new TradeEngine(stockDao, tradeDao);
        Stock stock = tradeEngine.createStock("Reliance");
        Stock stock1 = tradeEngine.createStock("Tata");
        Order buyRequest = new Order("BR1", "abc", OrderType.BUY, stock.getName(), 2, 300.0, LocalDateTime.now());
        Thread.sleep(10);
        Order buyRequest1 = new Order("BR2", "abcd", OrderType.BUY, stock.getName(), 6, 450.0, LocalDateTime.now());

        tradeEngine.executeBuyRequest(buyRequest);
        tradeEngine.executeBuyRequest(buyRequest1);
        Thread.sleep(50);
        Order buyRequest2 = new Order("BR3", "abcd", OrderType.BUY, stock.getName(), 1, 490.0, LocalDateTime.now());
        tradeEngine.executeBuyRequest(buyRequest2);
        Order sellReq = new Order("SR1", "abce", OrderType.SELL, stock.getName(), 8, 350.0, LocalDateTime.now());

        tradeEngine.executeSellRequest(sellReq);
        System.out.println(tradeEngine.getBuyPendingRequests(stock.getName()));
        System.out.println(tradeEngine.getSellPendingRequests(stock.getName()));

        Order sellReq1 = new Order("SR2", "abcf", OrderType.SELL, stock.getName(), 8, 150.0, LocalDateTime.now());

        tradeEngine.executeSellRequest(sellReq1);
        System.out.println(tradeEngine.getBuyPendingRequests(stock.getName()));
        System.out.println(tradeEngine.getSellPendingRequests(stock.getName()));

        System.out.println("Validate after me");
        Order buyRequest3 = new Order("BR4", "abcg", OrderType.BUY, stock.getName(), 10, 500, LocalDateTime.now());
        tradeEngine.executeBuyRequest(buyRequest3);
        System.out.println(tradeEngine.getBuyPendingRequests(stock.getName()));
        System.out.println(tradeEngine.getSellPendingRequests(stock.getName()));


        Order buyRequest4 = new Order("BR5", "abci", OrderType.BUY, stock1.getName(), 10, 500, LocalDateTime.now());
        Order sellReq3 = new Order("SR2", "abcf", OrderType.SELL, stock1.getName(), 8, 150.0, LocalDateTime.now());
        tradeEngine.executeBuyRequest(buyRequest4);
        System.out.println(tradeEngine.getBuyPendingRequests(stock1.getName()));
        System.out.println(tradeEngine.getSellPendingRequests(stock1.getName()));

        tradeEngine.executeSellRequest(sellReq3);
        System.out.println(tradeEngine.getBuyPendingRequests(stock1.getName()));
        System.out.println(tradeEngine.getSellPendingRequests(stock1.getName()));

        System.out.println(tradeEngine.getBuyPendingRequests(stock.getName()));
        System.out.println(tradeEngine.getSellPendingRequests(stock.getName()));
    }
}
