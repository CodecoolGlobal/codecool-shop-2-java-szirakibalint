package com.codecool.shop.logger;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Order;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoggerService {

    private final OrderDao orderDao;

    public LoggerService(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public void logOrder(int orderId) {
        Order order = orderDao.find(orderId);
        JSONObject jsonObject = new JSONObject(){{
            try {
                put("order", order);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};
        try {
            FileWriter file = new FileWriter("idk.json");
            file.write(jsonObject.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
