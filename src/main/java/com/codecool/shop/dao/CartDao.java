package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;

public interface CartDao {
    void createNewCart(int userId);
    Cart getCartByUserId(int userId);
    Cart getCartById(int cartId);
    int getProductQuantity(int cartId, Product product);
    void addToCart(int cartId, Product product);
    void removeOneFromCart(int cartId, Product product);
    void removeProductFromCart(int cartId, Product product);
    void removeAllFromCart(int cartId);
    BigDecimal getTotalSum(int cartId);
}
