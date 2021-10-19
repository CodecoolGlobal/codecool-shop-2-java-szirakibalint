package com.codecool.shop.dao;

import com.codecool.shop.model.Cart;

public interface CartDao {
    void createNewCart(int userId);
    Cart getCartByUserId(int userId);
    Cart getCartById(int cartId);
    void addToCart(int cartId, int productId);
    void removeOneFromCart(int cartId, int productId);
    void removeProductFromCart(int cartId, int productId);
    void removeAllFromCart(int cartId);

}
