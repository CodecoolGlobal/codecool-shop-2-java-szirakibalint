package com.codecool.shop.logger;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Order;
import com.codecool.shop.service.CartService;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class LoggerService {

    private final OrderDao orderDao;
    private final CartService cartService;

    public LoggerService(OrderDao orderDao, CartDao cartDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.cartService = new CartService(cartDao, productDao);
    }

    public void logOrder(int orderId) {
        Order order = orderDao.find(orderId);
        JSONObject orderJson = order.toJson();
        String filename = createFileName(order);
        try {
            createFile(filename);
            writeFile(filename, orderJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String createFileName(Order order) {
        String year = String.valueOf(order.getOrderedAt().getYear());
        String month = String.valueOf(order.getOrderedAt().getMonth());
        String day = String.valueOf(order.getOrderedAt().getDayOfMonth());
        return order.getId() + "-" + year + "-" + month + "-" + day + ".json";
    }

    private void createFile(String filename) throws IOException {
        File file = new File(filename);
        file.createNewFile();
    }

    private void writeFile(String filename, JSONObject orderJson) throws IOException {
        FileWriter fileWriter = new FileWriter(filename);
        String text = orderJson.toString();
        fileWriter.write(text);
        fileWriter.close();
    }
}
