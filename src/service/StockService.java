package service;

import dao.StockDao;
import entity.Order;
import entity.Stock;
import exception.InvalidStockException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class StockService implements IStockService{

    private final StockDao stockDao;

    public StockService(StockDao stockDao) {
        this.stockDao = stockDao;
    }

    @Override
    public Stock getStockDetails(String stockId) {

        return this.stockDao.getStock(stockId);
    }

    public Stock createStock(String stockId) {
        return this.stockDao.createStock(stockId);
    }

    /**
     * Here for a buy requests, trade could be performed between multiple old sell requests and new buy request. So,
     * when a trade is performed between a buy and sell request, it's left over quantity is updated and in case full
     * sell request is completed ( i.e. all the requested sell quantity achieved, then we delete this order pending orders.
     * @param orders  List of sell request orders with which buys request is fulfilled.
     * @param stock stock for which transaction is going on.
     * @param quantityMap map of order-id to quantity (i.e. with key as order-id how much quantity of stocks transaction
     *                    happened.).
     */
    @Override
    public void updateRemainingQuantityForSellerOrders(List<Order> orders, Stock stock, Map<String, Integer> quantityMap) {
        for (Order order : orders) {
            if (quantityMap.get(order.getOrderId()) == order.getLeftQuantity()) {
                stock.getSellOrders().remove(order);
            } else if (quantityMap.get(order.getOrderId()) < order.getLeftQuantity()) {
                int q = order.getLeftQuantity() - quantityMap.get(order.getOrderId());
                order.setLeftQuantity(q);
            }
        }
    }

    /**
     * Here for a sell requests, trade could be performed between multiple old buy requests and new sell request. So,
     * when a trade is performed between a buy and sell request, it's left over quantity is updated and in case full
     * buy request is completed ( i.e. all the requested buy quantity achieved, then we delete this order pending orders.
     * @param orders List of buy request orders with which sell request is fulfilled.
     * @param stock stock for which transaction is going on.
     * @param quantityMap map of order-id to quantity (i.e. with key as order-id how much quantity of stocks transaction
     *                    happened.).
     */
    @Override
    public void updateRemainingQuantityForBuyOrders(List<Order> orders, Stock stock, Map<String, Integer> quantityMap) {
        for (Order order : orders) {
            if (quantityMap.get(order.getOrderId()) == order.getLeftQuantity()) {
                stock.getBuyOrders().remove(order);
            } else if (quantityMap.get(order.getOrderId()) < order.getLeftQuantity()) {
                int q = order.getLeftQuantity() - quantityMap.get(order.getOrderId());
                order.setLeftQuantity(q);
            }
        }
    }

    /**
     * Return details of pending buy orders
     * @param stockId stock-id for which data is requested.
     * @return String containing details of pending buy orders.
     * @throws InvalidStockException in-case wrong-id stockId is passed as input.
     */
    @Override
    public String getBuyPendingRequests(String stockId) {
        Stock stock = stockDao.getStock(stockId);
        if (stock == null) {
            throw new InvalidStockException("Wrong Stock Name.");
        }
        return "Pending Buy Request for stock: " + stockId + " \n" + getStringResultsForOrder(stock.getBuyOrders());
        //return String.valueOf(stock.getBuyOrders().size());
    }

    /**
     * Return details of pending buy orders
     * @param stockId stock-id for which data is requested.
     * @return String containing details of pending buy orders.
     * @throws InvalidStockException in-case wrong-id stockId is passed as input.
     */
    @Override
    public String getSellPendingRequests(String stockId) {
        Stock stock = stockDao.getStock(stockId);
        if (stock == null) {
            throw new InvalidStockException("Wrong Stock Name.");
        }
        return "Pending Sell Request for stock: " + stockId + " \n" + getStringResultsForOrder(stock.getSellOrders());

    }

    private String getStringResultsForOrder(List<Order> orders) {
        StringBuilder sb = new StringBuilder();
        if (orders == null || orders.isEmpty()) {
            sb.append("No req pending.");
        } else {
            for (Order order : orders) {
                if (order.validTime == null || order.validTime.isAfter(LocalDateTime.now()))
                    sb.append("Order Id: ").append(order.getOrderId()).append(" Quantity: ").append(order.getLeftQuantity())
                            .append(" price: ").append(order.getPrice()).append("\n");
            }
        }

        return sb.toString();
    }
}
