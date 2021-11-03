package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.mapper.ProductMapper;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.codecool.shop.model.Supplier;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductCategoryDaoJDBC implements ProductCategoryDao{

    private final DataSource dataSource;
    private static ProductCategoryDao instance;
    private final ProductMapper productMapper = new ProductMapper();

    private ProductCategoryDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static ProductCategoryDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new ProductCategoryDaoJDBC(dataSource);
        }
        return instance;
    }

    public static ProductCategoryDao getInstance() {
        return instance;
    }

    @Override
    public void add(ProductCategory category) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO category (name, department, description) VALUES (?, ?, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, category.getName());
            statement.setString(2, category.getDepartment());
            statement.setString(3, category.getDescription());
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding product category");
        }
    }

    @Override
    public ProductCategory find(int id) {
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
                    "WHERE category.id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                ProductCategory category = new ProductCategory(resultSet.getString("category_name"),
                        resultSet.getString("category_department"),
                        resultSet.getString("category_description"));
                category.setId(resultSet.getInt("category_id"));
                Product product = productMapper.createProductFromResultSet(resultSet);
                product.setProductCategory(category);
                while(resultSet.next()) {
                    product = productMapper.createProductFromResultSet(resultSet);
                    product.setProductCategory(category);
                }
                return category;
            }
        } catch (SQLException e) {
            System.out.println("Error while finding product category");
        }
        return null;
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "DELETE FROM category WHERE id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while deleting category");
        }
    }

    @Override
    public List<ProductCategory> getAll() {
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
                    "ORDER BY category.id";
            PreparedStatement statement = conn.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            List<ProductCategory> categories = new ArrayList<>();
            int actualCategoryId = -1;
            ProductCategory actualCategory = null;
            while (resultSet.next()) {
                if (resultSet.getInt("category_id") != actualCategoryId) {
                    if (actualCategory != null) {
                        categories.add(actualCategory);
                    }
                    actualCategory = new ProductCategory(resultSet.getString("category_name"),
                            resultSet.getString("category_department"),
                            resultSet.getString("category_description"));
                    actualCategory.setId(resultSet.getInt("category_id"));
                    actualCategoryId = actualCategory.getId();
                }
                Product product = productMapper.createProductFromResultSet(resultSet);
                product.setProductCategory(actualCategory);
            }
            categories.add(actualCategory);
            return categories;
        } catch (SQLException e) {
            System.out.println("Error while getting product category list");
            return null;
        }
    }
}
