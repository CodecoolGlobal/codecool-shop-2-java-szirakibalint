package com.codecool.shop.config;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.dao.jdbc.*;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataBaseManager {

    private static DataBaseManager instance;

    private DataBaseManager() {
    }

    public static DataBaseManager getInstance() {
        if (instance == null) {
            instance = new DataBaseManager();
        }
        return instance;
    }

    public DataSource getConnectionData() {
        PGSimpleDataSource dataSource = new PGSimpleDataSource();
        dataSource.setDatabaseName(System.getenv("PSQL_DB"));
        dataSource.setUser(System.getenv("PSQL_NAME"));
        dataSource.setPassword(System.getenv("PSQL_PW"));
        try {
            dataSource.getConnection().close();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return dataSource;
    }

    public ProductDao getCurrentProductDao() {
        String config = System.getenv("config");
        if (config.equals("jdbc")) {
            return ProductDaoJDBC.getInstance();
        } else if (config.equals("memory")) {
            return ProductDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public ProductCategoryDao getCurrentProductCategoryDao() {
        String config = System.getenv("config");
        if (config.equals("jdbc")) {
            return ProductCategoryDaoJDBC.getInstance();
        } else if (config.equals("memory")) {
            return ProductCategoryDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public SupplierDao getCurrentSupplierDao() {
        String config = System.getenv("config");
        if (config.equals("jdbc")) {
            return SupplierDaoJDBC.getInstance();
        } else if (config.equals("memory")) {
            return SupplierDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public CartDao getCurrentCartDao() {
        String config = System.getenv("config");
        if (config.equals("jdbc")) {
            return CartDaoJDBC.getInstance();
        } else if (config.equals("memory")) {
            return CartDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public OrderDao getCurrentOrderDao() {
        String config = System.getenv("config");
        if (config.equals("jdbc")) {
            return OrderDaoJDBC.getInstance();
        } else if (config.equals("memory")) {
            return OrderDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }
}
