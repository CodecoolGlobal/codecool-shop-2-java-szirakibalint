package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Order;

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
        orderDao.getAll().forEach(System.out::println);
    }

    public int addNewOrder(int userId, int cartId, String lastName, String firstName, String country, String city, String address) {
        Order order = new Order(firstName, lastName, country, city, address, userId, cartDao.getCartById(cartId));
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
        System.out.println(order);
    }
}
