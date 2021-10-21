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
    private final CartDao cartDao;
    private final CartService cartService;

    public LoggerService(OrderDao orderDao, CartDao cartDao, ProductDao productDao) {
        this.orderDao = orderDao;
        this.cartDao = cartDao;
        this.cartService = new CartService(cartDao, productDao);
    }

    public void logValidOrder(int orderId) {
        Order order = orderDao.find(orderId);
        JSONObject orderJson = createJSONFromValidOrder(order);
        String filename = createFileName(order);
        try {
            createFile(filename);
            writeFile(filename, orderJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private JSONObject createJSONFromValidOrder(Order order) {
        return new JSONObject(){{
            try {
                put("status", "paid");
                put("name", order.getFirstName() + " " + order.getLastName());
                put("full_address", new JSONObject(){{
                    put("country", order.getCountry());
                    put("city", order.getCity());
                    put("address", order.getAddress());
                }});
                put("cart", cartService.createJsonFromCart(order.getCart()));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};
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
