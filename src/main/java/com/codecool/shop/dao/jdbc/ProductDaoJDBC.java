package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.mapper.ProductMapper;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoJDBC implements ProductDao {
    private static final Logger logger = LoggerFactory.getLogger(ProductDaoJDBC.class);

    private final DataSource dataSource;
    private static ProductDao instance;
    private final ProductMapper productMapper = new ProductMapper();

    private ProductDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ProductDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new ProductDaoJDBC(dataSource);
        }
        return instance;
    }

    public static ProductDao getInstance() {
        return instance;
    }

    @Override
    public void add(Product product) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO product (name, default_price, currency, description, category_id, supplier_id) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setBigDecimal(2, product.getDefaultPrice());
            statement.setString(3, String.valueOf(product.getDefaultCurrency()));
            statement.setString(4, product.getDescription());
            statement.setInt(5, product.getProductCategory().getId());
            statement.setInt(6, product.getSupplier().getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while adding product: '{}'", product.toString());
        }
    }

    @Override
    public Product find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "product.id AS product_id, " +
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
                    "WHERE product.id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return productMapper.createProductFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            logger.error("Error while finding product with id = '{}'", id);
        }
        return null;
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM product WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error while deleting product with id = '{}'", id);
        }
    }

    @Override
    public List<Product> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "product.id AS product_id, " +
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
                    "ORDER BY product.id";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<Product> products = new ArrayList<>();
            while (resultSet.next()) {
                Product product = productMapper.createProductFromResultSet(resultSet);
                products.add(product);
            }
            return products;
        } catch (SQLException e) {
            logger.error("Error while getting product list");
            return null;
        }
    }

    @Override
    public List<Product> getBy(Supplier supplier) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "product.id AS product_id, " +
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
                    "WHERE supplier.id = ?" +
                    "ORDER BY product.id";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, supplier.getId());
            ResultSet resultSet = statement.executeQuery();
            return productMapper.createProductList(resultSet);
        } catch (SQLException e) {
            logger.error("Error while getting product list by supplier: '{}'", supplier.toString());
            return null;
        }
    }

    @Override
    public List<Product> getBy(ProductCategory productCategory) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "product.id AS product_id, " +
                    "product.name AS product_name, " +
                    "product.default_price AS product_price, " +
                    "product.currency AS product_currency, " +
                    "product.description AS product_description, " +
                    "category.id AS category_id, " +
                    "category.name AS category_name, " +
                    "category.department AS category_department, " +
                    "category.description AS category_description " +
                    "FROM category " +
                    "LEFT JOIN product ON category.id = product.category_id " +
                    "LEFT JOIN supplier ON product.supplier_id = supplier.id " +
                    "WHERE category.id = ?" +
                    "ORDER BY product.id";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, productCategory.getId());
            ResultSet resultSet = statement.executeQuery();
            return productMapper.createProductList(resultSet);
        } catch (SQLException e) {
            logger.error("Error while getting product list by category: '{}'", productCategory.toString());
            return null;
        }
    }
}
