package com.codecool.shop.model;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Order {
    private static int idCounter = 0;

    private final int id;
    private LocalDateTime orderedAt;
    private boolean paidFor;
    private final int userId;
    private Map<Product, Integer> products;

    public Order(String firstName, String lastName, String country, String city, String address, int userId, Cart cart){
        this.firstName = firstName;
        this.lastName = lastName;
        this.country = country;
        this.city = city;
        this.address = address;
        this.id = idCounter;
        idCounter++;

        this.userId = userId;
        products = cart.getProducts();
        this.orderedAt = LocalDateTime.now();

    }

    public void pay(){
        if (!paidFor) {
            this.paidFor = true;
        } else {
            throw new RuntimeException("already paid for this!!");
        }
    }



}
