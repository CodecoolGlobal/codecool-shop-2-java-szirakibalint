package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;

import java.util.ArrayList;
import java.util.List;

public class CartDaoMem implements CartDao {

    private List<Cart> data = new ArrayList<>();
    private static CartDaoMem instance = null;
    private ProductDao productList = ProductDaoMem.getInstance();

    /* A private Constructor prevents any other class from instantiating.
     */
    private CartDaoMem() {
        data.add(new Cart());
    }

    public static CartDaoMem getInstance() {
        if (instance == null) {
            instance = new CartDaoMem();
        }
        return instance;
    }

    @Override
    public void createNewCart(int userId) {
        data.add(new Cart(userId));
    }

    @Override
    public Cart getCartByUserId(int userId) {
        return data.stream().filter(cart -> cart.getUserId() == userId).findFirst().orElse(null);
    }

    @Override
    public Cart getCartById(int cartId) {
        return data.stream().filter(cart -> cart.getId() == cartId).findFirst().orElse(null);
    }

    @Override
    public void addToCart(int cartId, int productId) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.add(productList.find(productId));
        }
    }

    @Override
    public void removeOneFromCart(int cartId, int productId) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.decrease(productList.find(productId));
        }
    }

    @Override
    public void removeProductFromCart(int cartId, int productId) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.remove(productList.find(productId));
        }
    }

    @Override
    public void removeAllFromCart(int cartId) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.removeAll();
        }
    }
}
