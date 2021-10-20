package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;

import java.math.BigDecimal;

public class PaymentService {

    private static final int DEFAULT_CART_ID = 0;

    private final CartDao cartDao;

    public PaymentService(CartDao cartDao) {
        this.cartDao = cartDao;
    }

    public BigDecimal getFullPriceForPayment(String cartId) {
        Cart cart = cartId == null
                ? cartDao.getCartById(DEFAULT_CART_ID)
                : cartDao.getCartById(Integer.parseInt(cartId));
        return cart.getTotalSum();
    }
}
