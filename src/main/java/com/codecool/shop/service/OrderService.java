package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;

import java.math.BigDecimal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

public class OrderService {

    private static final int DEFAULT_CART_ID = 0;

    private final CartDao cartDao;
    private final OrderDao orderDao;

    public OrderService(CartDao cartDao, OrderDao orderDao) {
        this.cartDao = cartDao;
        this.orderDao = orderDao;
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

    public void addNewOrder(int userId, int cartId, String lastName, String firstName, String country, String city, String address) {
        orderDao.add(new Order(firstName, lastName, country, city, address, userId, cartDao.getCartById(cartId)));
    }

    public boolean checkoutOrder(Map<String, String> params) {
        if (validateFormData(params)) {
            addNewOrder(0,0,
                    params.get("lastname"),
                    params.get("firstname"),
                    params.get("country"),
                    params.get("city"),
                    params.get("address"));
            return true;
        }
        return false;
    }

    private boolean validateFormData(Map<String, String> params){
        for (String value : params.values()){
            if (value.equals("")) {
                return false;
            };
        }
        return true;
    }
}
