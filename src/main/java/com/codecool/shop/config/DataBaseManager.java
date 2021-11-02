package com.codecool.shop.config;

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
}
