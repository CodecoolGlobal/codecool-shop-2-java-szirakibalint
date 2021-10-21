package com.codecool.shop.model;

import java.time.LocalDateTime;
import java.util.Map;

public class Order {
    private static int idCounter = 0;

    private final int id;
    private final String firstName;
    private final String lastName;
    private final String country;
    private final String city;
    private final String address;
    private LocalDateTime orderedAt;
    private boolean paidFor = false;
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

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public LocalDateTime getOrderedAt() {
        return orderedAt;
    }

    public boolean isPaidFor() {
        return paidFor;
    }

    public int getUserId() {
        return userId;
    }

    public Map<Product, Integer> getProducts() {
        return products;
    }

    public void pay(){
        if (!paidFor) {
            this.paidFor = true;
        } else {
            throw new RuntimeException("already paid for this!!");
        }
    }

    @Override
    public String toString() {
        return orderedAt +
                " " + firstName +
                " " + lastName +
                " " + address +
                " " + products.toString();
    }



}
