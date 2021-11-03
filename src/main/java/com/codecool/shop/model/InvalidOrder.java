package com.codecool.shop.model;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidOrder extends Order {
    private static final Logger logger = LoggerFactory.getLogger(InvalidOrder.class);

    private final String errorMessage;

    public InvalidOrder(String errorMessage, Cart cart) {
        super(cart);
        this.errorMessage = errorMessage;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(){{
            try {
                put("status", "failed");
                put("id", id);
                put("error_message", errorMessage);
                put("cart", cart.createJsonFromCart());
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }};
    }
}
