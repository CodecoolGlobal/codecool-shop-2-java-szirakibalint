package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.mapper.ProductMapper;
import com.codecool.shop.model.Order;

import javax.sql.DataSource;
import java.util.List;

public class OrderDaoJDBC implements OrderDao {

    private final DataSource dataSource;
    private static OrderDao instance;
    private final ProductMapper productMapper = new ProductMapper();

    private OrderDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static OrderDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new OrderDaoJDBC(dataSource);
        }
        return instance;
    }

    @Override
    public void add(Order order) {

    }

    @Override
    public Order find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Order> getAll() {
        return null;
    }
}
