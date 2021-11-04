package com.codecool.shop.model;

import org.json.JSONException;
import org.json.JSONObject;
import java.time.LocalDateTime;
import java.util.HashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InvalidOrder extends Order {
    private static final Logger logger = LoggerFactory.getLogger(InvalidOrder.class);

    private final String errorMessage;

    public InvalidOrder(String errorMessage, int cartId, String cart) {
        super(cart, cartId);
        this.errorMessage = errorMessage;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject(){{
            try {
                put("status", "failed");
                put("id", id);
                put("error_message", errorMessage);
                put("cart", cart);
            } catch (JSONException e) {
                logger.error("Error while creating JSONObject");
            }
        }};
    }

    @Override
    public HashMap<String, String> getRelevantInformation() {
        return new HashMap<>(){{
            put("valid", "false");
            put("cart_id", String.valueOf(cartId));
            put("cart_data", cart.toString());
            put("message", errorMessage);
        }};
    }
}
