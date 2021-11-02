package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
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
            String sql = "SELECT supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "product.id AS porduct_id, " +
                    "product.name AS product_name, " +
                    "product.default_price AS product_price, " +
                    "product.currency AS product_currency, " +
                    "product.description AS product_description, " +
                    "category.id AS category_id, " +
                    "category.name AS category_name, " +
                    "category.department AS category_department, " +
                    "category.description AS category_description " +
                    "FROM supplier " +
                    "LEFT JOIN product ON supplier.id = product.supplier_id " +
                    "LEFT JOIN category ON category.id = product.category_id " +
                    "WHERE supplier.id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                Supplier supplier = new Supplier(resultSet.getString("supplier_name"), resultSet.getString("supplier_description"));
                supplier.setId(resultSet.getInt("supplier_id"));
                Product product = createProductFromResultSet(resultSet);
                supplier.addProduct(product);
                while(resultSet.next()) {
                    product = createProductFromResultSet(resultSet);
                    supplier.addProduct(product);
                }
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
            String sql = "SELECT supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "product.id AS porduct_id, " +
                    "product.name AS product_name, " +
                    "product.default_price AS product_price, " +
                    "product.currency AS product_currency, " +
                    "product.description AS product_description, " +
                    "category.id AS category_id, " +
                    "category.name AS category_name, " +
                    "category.department AS category_department, " +
                    "category.description AS category_description " +
                    "FROM supplier " +
                    "LEFT JOIN product ON supplier.id = product.supplier_id " +
                    "LEFT JOIN category ON category.id = product.category_id " +
                    "ORDER BY supplier.id";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<Supplier> suppliers = new ArrayList<>();
            int actualSupplierId = -1;
            Supplier actualSupplier = null;
            while (resultSet.next()) {
                if (resultSet.getInt("supplier_id") != actualSupplierId) {
                    if (actualSupplier != null) {
                        suppliers.add(actualSupplier);
                    }
                    actualSupplier = new Supplier(resultSet.getString("supplier_name"), resultSet.getString("supplier_description"));
                    actualSupplier.setId(resultSet.getInt("supplier_id"));
                    actualSupplierId = actualSupplier.getId();
                }
                Product product = createProductFromResultSet(resultSet);
                if (actualSupplier != null) {
                    actualSupplier.addProduct(product);
                }
            }
            suppliers.add(actualSupplier);
            return suppliers;
        } catch (SQLException e) {
            System.out.println("Error while getting supplier list");
            return null;
        }
    }

    private Product createProductFromResultSet(ResultSet resultSet) throws SQLException {
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
    }
}
