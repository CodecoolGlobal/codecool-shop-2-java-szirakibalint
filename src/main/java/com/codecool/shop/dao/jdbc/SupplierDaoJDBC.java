package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.mapper.ProductMapper;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDaoJDBC implements SupplierDao{
    private static final Logger logger = LoggerFactory.getLogger(SupplierDaoJDBC.class);

    private final DataSource dataSource;
    private static SupplierDao instance;
    private final ProductMapper productMapper = new ProductMapper();

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
            logger.error("Error while adding supplier: '{}'", supplier.toString());
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
                Product product = productMapper.createProductFromResultSet(resultSet);
                product.setSupplier(supplier);
                while(resultSet.next()) {
                    product = productMapper.createProductFromResultSet(resultSet);
                    product.setSupplier(supplier);
                }
                return supplier;
            }
        } catch (SQLException e) {
            logger.error("Error while finding supplier with id = '{}'", id);
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
            logger.error("Error while deleting supplier with id = '{}'", id);
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
                Product product = productMapper.createProductFromResultSet(resultSet);
                product.setSupplier(actualSupplier);
            }
            suppliers.add(actualSupplier);
            return suppliers;
        } catch (SQLException e) {
            logger.error("Error while getting supplier list");
            return null;
        }
    }
}
