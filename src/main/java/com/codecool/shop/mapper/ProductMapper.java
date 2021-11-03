package com.codecool.shop.mapper;

import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductMapper {

    public Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
        Supplier supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
        supplier.setId(resultSet.getInt("id"));
        ProductCategory category = new ProductCategory(resultSet.getString("category_name"),
                resultSet.getString("category_department"),
                resultSet.getString("category_description"));
        category.setId(resultSet.getInt("category_id"));
        Product product = new Product(resultSet.getString("product_name"),
                resultSet.getBigDecimal("product_price"),
                resultSet.getString("product_currency"),
                resultSet.getString("product_description"),
                category,
                supplier);
        product.setId(resultSet.getInt("product_id"));
        return product;
    }

    public List<Product> createProductList(ResultSet resultSet) throws SQLException {
        List<Product> products = new ArrayList<>();
        while (resultSet.next()) {
            Product product = createProductFromResultSet(resultSet);
            products.add(product);
        }
        return products;
    }
}
