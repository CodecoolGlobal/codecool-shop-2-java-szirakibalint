package com.codecool.shop.config;

import com.codecool.shop.dao.*;
import com.codecool.shop.dao.implementation.*;
import com.codecool.shop.dao.jdbc.*;
import org.postgresql.ds.PGSimpleDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.SQLException;

public class DataBaseManager {
    private static final Logger logger = LoggerFactory.getLogger(DataBaseManager.class);

    private static DataBaseManager instance;

    private static final String ENVIRONMENTAL_VARIABLE_FOR_CONFIG = "config";
    private static final String DATABASE_KEYWORD = "jdbc";
    private static final String MEMORY_KEYWORD = "memory";

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
            logger.error("Error while getting connection data");
        }
        return dataSource;
    }

    public ProductDao getCurrentProductDao() {
        String config = System.getenv(ENVIRONMENTAL_VARIABLE_FOR_CONFIG);
        if (config.equals(DATABASE_KEYWORD)) {
            return ProductDaoJDBC.getInstance();
        } else if (config.equals(MEMORY_KEYWORD)) {
            return ProductDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public ProductCategoryDao getCurrentProductCategoryDao() {
        String config = System.getenv(ENVIRONMENTAL_VARIABLE_FOR_CONFIG);
        if (config.equals(DATABASE_KEYWORD)) {
            return ProductCategoryDaoJDBC.getInstance();
        } else if (config.equals(MEMORY_KEYWORD)) {
            return ProductCategoryDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public SupplierDao getCurrentSupplierDao() {
        String config = System.getenv(ENVIRONMENTAL_VARIABLE_FOR_CONFIG);
        if (config.equals(DATABASE_KEYWORD)) {
            return SupplierDaoJDBC.getInstance();
        } else if (config.equals(MEMORY_KEYWORD)) {
            return SupplierDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public CartDao getCurrentCartDao() {
        String config = System.getenv(ENVIRONMENTAL_VARIABLE_FOR_CONFIG);
        if (config.equals(DATABASE_KEYWORD)) {
            return CartDaoJDBC.getInstance();
        } else if (config.equals(MEMORY_KEYWORD)) {
            return CartDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }

    public OrderDao getCurrentOrderDao() {
        String config = System.getenv(ENVIRONMENTAL_VARIABLE_FOR_CONFIG);
        if (config.equals(DATABASE_KEYWORD)) {
            return OrderDaoJDBC.getInstance();
        } else if (config.equals(MEMORY_KEYWORD)) {
            return OrderDaoMem.getInstance();
        } else {
            throw new RuntimeException("Invalid config setting");
        }
    }
}
