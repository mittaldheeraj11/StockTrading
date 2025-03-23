package service;

import entity.Order;
import entity.TradeResponse;

public interface ITradeService {

    /**
     * Method to perform transactions when a buy -order request is placed.
     *
     * @param buyOrderRequest details about the buy request order.
     * @return
     */
    TradeResponse executeBuyRequest(Order buyOrderRequest);

    /**
     * Method to perform transactions when a sell -order request is placed.
     *
     * @param sellOrderRequest details about the sell request order.
     * @return
     */
    TradeResponse executeSellRequest(Order sellOrderRequest);
}
