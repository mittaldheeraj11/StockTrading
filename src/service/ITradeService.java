package service;

import entity.Order;
import entity.Trade;

import java.util.List;
import java.util.Map;

public interface ITradeService {

    /**
     * Method to perform transactions when a buy -order request is placed.
     * @param buyOrderRequest details about the buy request order.
     */
    void executeBuyRequest(Order buyOrderRequest);

    /**
     * Method to perform transactions when a sell -order request is placed.
     * @param sellOrderRequest details about the sell request order.
     */
    void executeSellRequest(Order sellOrderRequest);
}
