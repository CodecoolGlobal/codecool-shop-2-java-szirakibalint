package com.codecool.shop.mapper;

import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;

import java.sql.ResultSet;
import java.sql.SQLException;

public class CartMapper {

    private final ProductMapper productMapper = new ProductMapper();

    public Cart createCartFromResultSet(ResultSet resultSet) throws SQLException {
        Cart cart = new Cart();
        cart.setId(resultSet.getInt("cart_id"));
        cart.setUserId(resultSet.getInt("user_id"));
        Product product = productMapper.createProductFromResultSet(resultSet);
        cart.add(product);
        while(resultSet.next()) {
            product = productMapper.createProductFromResultSet(resultSet);
            cart.add(product);
        }
        return cart;
    }
}
