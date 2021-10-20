package com.codecool.shop.dao;

import com.codecool.shop.model.Order;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;


public interface OrderDao {
    void add(Order order);
    Order find(int id);
    void remove(int id);




}
