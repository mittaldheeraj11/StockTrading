package service;

import entity.Order;

public interface ITradeService {

    void executeBuyRequest(Order buyOrderRequest);

    void executeSellRequest(Order sellOrderRequest);
}
