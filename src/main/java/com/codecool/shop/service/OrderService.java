package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.logger.LoggerService;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.InvalidOrder;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.ValidOrder;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class OrderService {

    private static final int DEFAULT_CART_ID = 0;
    private static final int DEFAULT_USER_ID = 0;

    private final CartDao cartDao;
    private final OrderDao orderDao;
    private final ProductDao productDao;

    public OrderService(CartDao cartDao, OrderDao orderDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.orderDao = orderDao;
        this.productDao = productDao;
    }

    public BigDecimal getFullPriceForPayment(String cartId) {
        Cart cart = cartId == null
                ? cartDao.getCartById(DEFAULT_CART_ID)
                : cartDao.getCartById(Integer.parseInt(cartId));
        return cart.getTotalSum();
    }

    public void printALlOrders(){
        orderDao.getAll().forEach(System.out::println);
    }

    public int addNewValidOrder(int userId, int cartId, String lastName, String firstName, String country, String city, String address) {
        Cart cart = cartDao.getCartById(cartId);
        Order order = new ValidOrder(firstName, lastName, country, city, address, userId, cart.getId(), cart.createJsonFromCart().toString());
        orderDao.add(order);
        return order.getId();
    }

    public int addNewInvalidOrder(String message, Cart cart) {
        Order order = new InvalidOrder(message, cart.getId(), cart.createJsonFromCart().toString());
        orderDao.add(order);
        return order.getId();
    }

    public void payForOrder(String order_id) {
        Order order = (order_id == null)
                ? orderDao.find(0)
                : orderDao.find(Integer.parseInt(order_id));
        if (order != null) {
            order.pay();
        }
    }

    public Integer checkoutOrder(Map<String, String> params) {
        if (validateFormData(params)) {
            int orderId = addNewValidOrder(DEFAULT_USER_ID,DEFAULT_CART_ID,
                    params.get("lastname"),
                    params.get("firstname"),
                    params.get("country"),
                    params.get("city"),
                    params.get("address"));
            return orderId;
        }
        return null;
    }

    private boolean validateFormData(Map<String, String> params){
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (entry.getValue().equals("")) {
                int orderId = addNewInvalidOrder("missing " + entry.getKey(), cartDao.getCartById(DEFAULT_CART_ID));
                LoggerService loggerService = new LoggerService(orderDao, cartDao, productDao);
                loggerService.logOrder(orderId);
                return false;
            }
        }
        return true;
    }
}
