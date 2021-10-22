package com.codecool.shop.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
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

    public int getId() {
        return id;
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

    public BigDecimal getTotalSum() {
        BigDecimal totalSum = BigDecimal.ZERO;
        for (Map.Entry<Product, Integer> productIntegerEntry : products.entrySet()) {
            Product product = productIntegerEntry.getKey();
            String valueString = String.valueOf(productIntegerEntry.getValue());
            BigDecimal multipliedValue = product.getDefaultPrice().multiply(new BigDecimal(valueString));
            totalSum = totalSum.add(multipliedValue);
        }
        return totalSum;
    }

    public JSONObject createJsonFromCart() {
        List<JSONObject> products = createJsonFromCartContent();
        BigDecimal totalSum = this.getTotalSum();
        return new JSONObject() {{
            try {
                put("id", id);
                put("products", products);
                put("total_price", String.valueOf(totalSum));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};
    }

    private List<JSONObject> createJsonFromCartContent() {
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
