package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.ProductCategory;

import javax.sql.DataSource;
import java.util.List;

public class ProductCategoryDaoJDBC implements ProductCategoryDao{

    private final DataSource dataSource;
    private static ProductCategoryDao instance;

    private ProductCategoryDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ProductCategoryDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC(dataSource);
        }
        return instance;
    }

    @Override
    public void add(ProductCategory category) {

    }

    @Override
    public ProductCategory find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<ProductCategory> getAll() {
        return null;
    }
}
