package com.codecool.shop.model;

import org.json.JSONObject;

import javax.management.remote.JMXServerErrorException;
import java.time.LocalDateTime;
import java.util.Map;

public abstract class Order {
    private static int idCounter = 0;

    protected final int id;
    protected final LocalDateTime orderedAt;
    private boolean paidFor = false;
    protected final Cart cart;

    public Order(Cart cart) {
        this.id = idCounter;
        idCounter++;
        this.orderedAt = LocalDateTime.now();
        this.cart = cart;
    }

    public int getId() {
        return id;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public void pay(){
        if (!paidFor && this instanceof ValidOrder) {
            this.paidFor = true;
        } else {
            throw new RuntimeException("already paid for this!!");
        }
    }

    public abstract JSONObject toJson();
}
