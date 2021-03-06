package com.codecool.shop.dao.implementation;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CartDaoMem implements CartDao {

    private final List<Cart> data = new ArrayList<>();
    private static CartDaoMem instance = null;

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
    public int getProductQuantity(int cartId, Product product) {
        Cart cart = data.get(cartId);
        Integer quantity = cart.getProducts().get(product);
        return Objects.requireNonNullElse(quantity, 0);
    }

    @Override
    public void addToCart(int cartId, Product product) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.add(product);
        }
    }

    @Override
    public void removeOneFromCart(int cartId, Product product) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.decrease(product);
        }
    }

    @Override
    public void removeProductFromCart(int cartId, Product product) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.remove(product);
        }
    }

    @Override
    public void removeAllFromCart(int cartId) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            cart.removeAll();
        }
    }

    @Override
    public BigDecimal getTotalSum(int cartId) {
        Cart cart = getCartById(cartId);
        if (cart != null) {
            return cart.getTotalSum();
        }
        return BigDecimal.ZERO;
    }
}
