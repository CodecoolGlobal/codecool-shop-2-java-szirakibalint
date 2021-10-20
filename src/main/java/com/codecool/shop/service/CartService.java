package com.codecool.shop.service;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartService {

    private static final int DEFAULT_CART_ID = 0;

    private final CartDao cartDao;
    private final ProductDao productDao;

    public CartService(CartDao cartDao, ProductDao productDao) {
        this.cartDao = cartDao;
        this.productDao = productDao;
    }

    public void addToCart(String productId, String userId) {
        try {
            int cartId = (userId == null)
                    ? DEFAULT_CART_ID
                    : cartDao.getCartByUserId(Integer.parseInt(userId)).getId();
            if (productId == null) {
                return;
            }
            Product product = productDao.find(Integer.parseInt(productId));
            cartDao.addToCart(cartId, product);
        } catch (NullPointerException npe) {
            npe.printStackTrace();
        }
    }

    public List<JSONObject> handleGet(String userId, String productId) {
        Cart cart = (userId != null)
                ? cartDao.getCartByUserId(Integer.parseInt(userId))
                : cartDao.getCartById(DEFAULT_CART_ID);
        if (productId == null) {
            if (cart != null) {
                return createJsonFromCart(cart);
            } else {
                return new ArrayList<>();
            }
        } else {
            Product product = productDao.find(Integer.parseInt(productId));
            int quantity = cartDao.getProductQuantity(cart.getId(), product);
            JSONObject quantityJson = new JSONObject(quantity);
            List<JSONObject> listJson = new ArrayList<>(){{
                add(quantityJson);
            }};
            return listJson;
        }
    }

    public void handleDelete(String userId, String productId, String deleteType) {
        int cartId = (userId == null) ? DEFAULT_CART_ID : Integer.parseInt(userId);
        if (productId == null) {
            cartDao.removeAllFromCart(cartId);
        } else {
            Product product = productDao.find(Integer.parseInt(productId));
            if (deleteType.equals("single")) {
                cartDao.removeOneFromCart(cartId, product);
            } else if (deleteType.equals("multiple")) {
                cartDao.removeProductFromCart(cartId, product);
            } else {
                throw new RuntimeException("Invalid delete type");
            }
        }
    }

    private List<JSONObject> createJsonFromCart(Cart cart) {
        Map<Product, Integer> products = cart.getProducts();
        List<JSONObject> cartJson = new ArrayList<>();
        for (Product product : products.keySet()) {
            JSONObject newJson = new JSONObject(){{
                try {
                    put("id", product.getId());
                    put("name", product.getName());
                    put("price", product.getDefaultPrice());
                    put("category", product.getProductCategory().getName());
                    put("supplier", product.getSupplier().getName());
                    put("quantity", products.get(product));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }};
            cartJson.add(newJson);
        }
        return cartJson;
    }
}
