package com.codecool.shop.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ValidOrder extends Order {

    private final String firstName;
    private final String lastName;
    private final String country;
    private final String city;
    private final String address;
    private final int userId;

    public ValidOrder(String firstName, String lastName, String country, String city, String address, int userId, int cartId, String cart) {
        super(cart, cartId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.city = city;
        this.address = address;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return orderedAt +
                " " + firstName +
                " " + lastName +
                " " + address +
                " " + cart.toString();
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
                put("cart", cart);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};
    }

    @Override
    public HashMap<String, String> getRelevantInformation() {
        return new HashMap<>(){{
           put("valid", "true");
           put("cart_id", String.valueOf(cartId));
           put("first_name", firstName);
           put("last_name", lastName);
           put("country", country);
           put("city", city);
           put("address", address);
           put("user_id", String.valueOf(userId));
        }};
    }
}
