package com.codecool.shop.logger;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import org.json.JSONObject;

public class LoggerService {

    private final OrderDao orderDao;

    public LoggerService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void logOrder(int orderId) {
        Order order = orderDao.find(orderId);
        JSONObject jsonObject = new JSONObject(){{
            
        }};
    }
}
