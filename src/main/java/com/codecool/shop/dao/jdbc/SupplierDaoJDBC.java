package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
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
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO supplier (name, description) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, supplier.getName());
            statement.setString(2, supplier.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding supplier");
        }
    }

    @Override
    public Supplier find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM supplier WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                supplier.setId(resultSet.getInt("id"));
                return supplier;
            }
        } catch (SQLException e) {
            System.out.println("Error while finding supplier");
        }
        return null;
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM supplier WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting supplier");
        }
    }

    @Override
    public List<Supplier> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT * FROM supplier";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<Supplier> suppliers = new ArrayList<>();
            while (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("name"), resultSet.getString("description"));
                supplier.setId(resultSet.getInt("id"));
                suppliers.add(supplier);
            }
            return suppliers;
        } catch (SQLException e) {
            System.out.println("Error while finding supplier");
            return null;
        }
    }
}
