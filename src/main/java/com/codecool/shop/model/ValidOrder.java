package com.codecool.shop.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

public class ValidOrder extends Order {

    private final String firstName;
    private final String lastName;
    private final String country;
    private final String city;
    private final String address;
    private final int userId;
    private final Map<Product, Integer> products;

    public ValidOrder(String firstName, String lastName, String country, String city, String address, int userId, Cart cart) {
        super(cart);
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.city = city;
        this.address = address;
        this.userId = userId;
        this.products = cart.getProducts();
    }

    public Cart getCart() {
        return cart;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    @Override
    public String toString() {
        return orderedAt +
                " " + firstName +
                " " + lastName +
                " " + address +
                " " + products.toString();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(){{
            try {
                put("status", "paid");
                put("order_id", id);
                put("name", firstName + " " + lastName);
                put("user_id", userId);
                put("full_address", new JSONObject(){{
                    put("country", country);
                    put("city", city);
                    put("address", address);
                }});
                put("cart", cart.createJsonFromCart());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};
    }
}
