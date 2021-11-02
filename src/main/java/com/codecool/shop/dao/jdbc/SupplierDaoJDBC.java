package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao{

    private final DataSource dataSource;
    private static SupplierDao instance;

    private SupplierDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static SupplierDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new SupplierDaoJDBC(dataSource);
        }
        return instance;
    }

    @Override
    public void add(Supplier supplier) {
        
    }

    @Override
    public Supplier find(int id) {
        return null;
    }

    @Override
    public void remove(int id) {

    }

    @Override
    public List<Supplier> getAll() {
        return null;
    }
}
