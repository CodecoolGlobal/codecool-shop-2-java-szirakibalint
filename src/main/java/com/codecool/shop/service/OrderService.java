package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Cart;

import java.math.BigDecimal;

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
        System.out.println(orderDao.getAll());
    }
}
