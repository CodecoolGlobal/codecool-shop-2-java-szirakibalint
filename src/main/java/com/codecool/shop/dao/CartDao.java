package com.codecool.shop.dao;

public interface CartDao {

    void createNewCart();
    void getCartByUserId(int userId);
    void addToCart(int cartId, int productId);
    void removeOneFromCart(int cartId, int productId);
    void removeProductFromCart(int cartId, int productId);
    void removeAllFromCart(int cartId);

}
