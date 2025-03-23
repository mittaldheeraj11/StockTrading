package service;

import dao.TradeDao;
import entity.Order;
import entity.Stock;
import entity.Trade;
import entity.TradeResponse;
import enums.OrderType;
import exception.InvalidStockException;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static service.TradeServiceHelper.*;

public class TradeService implements ITradeService{

    private final IStockService service;

    private final TradeDao tradeDao;

    public TradeService(IStockService service, TradeDao tradeDao) {
        this.service = service;
        this.tradeDao = tradeDao;
    }

    /**
     * This method first validates the stock-id.
     *
     * @param sellOrderRequest details about the sell request order.
     * @return TradeResponse containing details about the breakup of trade happened.
     * @throws InvalidStockException in-case wrong-id stockId is passed as input.
     * Then it extracts the list of available old buy requests, where price is greater than the sell-order request price.
     * Then one-by-one tries to match the @param sellOrderRequest  with the available old buy requests and
     * creates the trade between new sell request and old buy requests. In case old buy request is completed, it would
     * remove that buy request. In case new sell request is not fullfilled, added the same in pending sell requests.
     */
    public TradeResponse executeSellRequest(@NotNull Order sellOrderRequest) {
        System.out.println("Got request to execute sell stock: " + sellOrderRequest.getStock() + " quantity: " + sellOrderRequest.getQuantity()
        + " price: " + sellOrderRequest.getPrice());
        Stock stock = this.service.getStockDetails(sellOrderRequest.getStock());
        if (stock == null) {
            throw new InvalidStockException("Not a valid stock-id/name.");
        }
        List<Order> buyOrder = stock.getBuyOrders();
        int quantityRequired = sellOrderRequest.getQuantity();
        synchronized (stock) {

            List<Order> list = buyOrder.stream()
                    .filter(bo -> bo.getPrice() >= sellOrderRequest.getPrice())
                    .filter(bo -> {
                        if (bo.validTime == null) {
                            return true;
                        } else {
                            if (bo.validTime.isBefore(sellOrderRequest.getOrderPlacedTime())) {
                                return false;
                            }
                            return true;
                        }
                    } )
                    .sorted(Comparator.comparing(Order::getOrderPlacedTime)).toList();
            Queue<Order> availableBuyOrders = new LinkedList<>(list);

            if (availableBuyOrders.isEmpty()) {
                // order can't be placed, add in queue.
                System.out.println("Adding order in pending req " + sellOrderRequest.getOrderId() + " quantity " +
                        sellOrderRequest.getLeftQuantity() + " price " + sellOrderRequest.getPrice());
                stock.getSellOrders().add(sellOrderRequest);
                return new TradeResponse(sellOrderRequest.getOrderType(), new HashMap<>(), sellOrderRequest.getQuantity());

            } else {
                OrderBreakupDetails orderBreakupDetails = getQuantityLeftAndCreateOrder(availableBuyOrders, quantityRequired, sellOrderRequest.getPrice());

                if (!orderBreakupDetails.getExecutedOrders().isEmpty()) {
                    List<Trade> tradeCreatedForSell = createTradeForSell(sellOrderRequest, orderBreakupDetails);
                    tradeDao.addTrade(tradeCreatedForSell);
                    service.updateRemainingQuantityForBuyOrders(orderBreakupDetails.getExecutedOrders(), stock, orderBreakupDetails.getQuantityMap());
                }


                if (orderBreakupDetails.getQuantityRemaining() > 0) {
                    System.out.println("Adding order in pending sell req " + sellOrderRequest.getOrderId() + " quantity " +
                            orderBreakupDetails.getQuantityRemaining() + " price " + sellOrderRequest.getPrice()) ;
                    sellOrderRequest.setLeftQuantity(orderBreakupDetails.getQuantityRemaining());
                    stock.getSellOrders().add(sellOrderRequest);
                }
                return createResponse(orderBreakupDetails, sellOrderRequest);
            }
        }

    }

    /**
     * This method first validates the stock-id.
     *
     * @param buyOrderRequest details about the buy request order.
     * @return TradeResponse containing details about the breakup of trade happened.
     * @throws InvalidStockException in-case wrong-id stockId is passed as input.
     * Then it extracts the list of available old sell requests, where price is lesser than the buy-order request price.
     * Then one-by-one tries to match the @param  buyOrderRequest with the available old sell requests and
     * creates the trade between new buy request and old sell requests. In case old sell request is completed, it would
     * remove that pending sell request. In case new buy request is not full-filled, added the same in pending buy requests.
     */
    public TradeResponse executeBuyRequest(@NotNull final Order buyOrderRequest) {
        System.out.println("Got request to execute buy stock: " + buyOrderRequest.getStock() + " quantity: " + buyOrderRequest.getQuantity()
                + " price: " + buyOrderRequest.getPrice());
        Stock stock = this.service.getStockDetails(buyOrderRequest.getStock());
        if (stock == null) {
            throw new InvalidStockException("Not a valid stock-id/name.");
        }

        List<Order> sellOrders = stock.getSellOrders();

        synchronized (stock) {
            List<Order> list = sellOrders.stream()
                    .filter(so -> so.getPrice() <= buyOrderRequest.getPrice())
                    .filter(so -> {
                        if (so.validTime == null) {
                            return true;
                        } else {
                            if (so.validTime.isBefore(buyOrderRequest.getOrderPlacedTime())) {
                                return false;
                            }
                            return true;
                        }
                    } )
                    .sorted(Comparator.comparing(Order::getOrderPlacedTime)).toList();
            Queue<Order> availableSellOrders = new LinkedList<>(list);
            if (availableSellOrders.isEmpty()) {
                // order can't be executed, add in queue.
                System.out.println("Adding order in pending buy req " + buyOrderRequest.getOrderId() + " quantity " +
                        buyOrderRequest.getQuantity() + " price " + buyOrderRequest.getPrice());
                stock.getBuyOrders().add(buyOrderRequest);
                TradeResponse tradeResponse = new TradeResponse(OrderType.BUY, new HashMap<>(), buyOrderRequest.getQuantity());
                return tradeResponse;

            } else {

                // quantityLeft -- remaining.
                OrderBreakupDetails orderBreakupDetails = getQuantityLeftCreateOrdersAndCreateOrdersMatched(availableSellOrders, buyOrderRequest.getQuantity());

                if (!orderBreakupDetails.getExecutedOrders().isEmpty()) {
                    List<Trade> tradesForBuyRequest = createTradeForBuy(buyOrderRequest, orderBreakupDetails);
                    tradeDao.addTrade(tradesForBuyRequest);
                    service.updateRemainingQuantityForSellerOrders(orderBreakupDetails.getExecutedOrders(), stock, orderBreakupDetails.getQuantityMap());
                }
                if (orderBreakupDetails.getQuantityRemaining() > 0) {
                    System.out.println("Adding order in pending buy req " + buyOrderRequest.getOrderId() + " quantity " +
                            orderBreakupDetails.getQuantityRemaining() + " price " + buyOrderRequest.getPrice());
                    buyOrderRequest.setLeftQuantity(orderBreakupDetails.getQuantityRemaining());
                    stock.getBuyOrders().add(buyOrderRequest);

                }
                return createResponse(orderBreakupDetails, buyOrderRequest);
            }
        }
    }

}
