package com.codecool.shop.model;

import java.util.*;

public class Cart {

    public static final int DEFAULT_USER_ID = 0;

    private static int cartNumber = 0;

    private final int id;
    private final int userId;
    private Map<Product, Integer> products = new HashMap<>();

    public Cart() {
        this.id = cartNumber;
        cartNumber++;
        this.userId = DEFAULT_USER_ID;
    }

    public Cart(int userId) {
        this.id = cartNumber;
        cartNumber++;
        this.userId = userId;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public int getUserId() {
        return userId;
    }

    public void add(Product product) {
        Integer productNum = products.get(product);
        productNum = (productNum == null) ? 1 : productNum + 1;
        products.put(product, productNum);
    }

    public void add(HashMap<Product, Integer> productList) {
        for (Map.Entry<Product, Integer> productIntegerEntry : productList.entrySet()) {
            Product key = productIntegerEntry.getKey();
            Integer value = productIntegerEntry.getValue();
            Integer productNum = products.get(key);
            productNum = (productNum == null) ? value : productNum + value;
            products.put(key, productNum);
        }
    }

    public void decrease(Product product) {
        int productNum = products.get(product);
        if (productNum != 0) {
            productNum--;
            products.put(product, productNum);
            if (productNum == 0) {
                products.remove(product);
            }
        }

    }

    public void remove(Product product) {
        products.remove(product);
    }

    public void removeAll() {
        products = new HashMap<>();
    }
}
