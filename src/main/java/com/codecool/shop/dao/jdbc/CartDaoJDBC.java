package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.CartDao;
import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.mapper.CartMapper;
import com.codecool.shop.mapper.ProductMapper;
import com.codecool.shop.model.Cart;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.*;

public class CartDaoJDBC implements CartDao {

    private final DataSource dataSource;
    private static CartDao instance;
    private final ProductMapper productMapper = new ProductMapper();
    private final CartMapper cartMapper = new CartMapper();

    private CartDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static CartDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new CartDaoJDBC(dataSource);
        }
        return instance;
    }

    @Override
    public void createNewCart(int userId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "INSERT INTO cart (id, user_id) VALUES (DEFAULT, ?)";
            PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while creating cart");
        }
    }

    @Override
    public Cart getCartByUserId(int userId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT cart.id AS cart_id, " +
                    "cart.id AS user_id, " +
                    "product.id AS product_id, " +
                    "product.name AS product_name, " +
                    "product.default_price AS product_price, " +
                    "product.currency AS product_currency, " +
                    "product.description AS product_description, " +
                    "supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "category.id AS category_id, " +
                    "category.name AS category_name, " +
                    "category.department AS category_department, " +
                    "category.description AS category_description " +
                    "FROM cart " +
                    "LEFT JOIN cart_product ON cart.id = cart_product.cart_id " +
                    "LEFT JOIN product ON cart_product.product_id = product.id " +
                    "LEFT JOIN supplier ON product.supplier_id = supplier.id " +
                    "LEFT JOIN category ON product.category_id = category.id " +
                    "WHERE cart.user_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return cartMapper.createCartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error while finding cart by id");
        }
        return null;
    }

    @Override
    public Cart getCartById(int cartId) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT cart.id AS cart_id, " +
                    "cart.id AS user_id, " +
                    "product.id AS product_id, " +
                    "product.name AS product_name, " +
                    "product.default_price AS product_price, " +
                    "product.currency AS product_currency, " +
                    "product.description AS product_description, " +
                    "supplier.id AS supplier_id, " +
                    "supplier.name AS supplier_name, " +
                    "supplier.description AS supplier_description, " +
                    "category.id AS category_id, " +
                    "category.name AS category_name, " +
                    "category.department AS category_department, " +
                    "category.description AS category_description " +
                    "FROM cart " +
                    "LEFT JOIN cart_product ON cart.id = cart_product.cart_id " +
                    "LEFT JOIN product ON cart_product.product_id = product.id " +
                    "LEFT JOIN supplier ON product.supplier_id = supplier.id " +
                    "LEFT JOIN category ON product.category_id = category.id " +
                    "WHERE cart.id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return cartMapper.createCartFromResultSet(resultSet);
            }
        } catch (SQLException e) {
            System.out.println("Error while finding cart by user id");
        }
        return null;
    }

    @Override
    public int getProductQuantity(int cartId, Product product) {
        try (Connection conn = dataSource.getConnection()) {
            String sql = "SELECT COUNT(*) AS count FROM cart_product WHERE product_id = ? AND cart_id = ?";
            PreparedStatement statement = conn.prepareStatement(sql);
            statement.setInt(1, product.getId());
            statement.setInt(2, cartId);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("count");
            }
        } catch (SQLException e) {
            System.out.println("Error while finding cart by user id");
        }
        return 0;
    }

    @Override
    public void addToCart(int cartId, Product product) {

    }

    @Override
    public void removeOneFromCart(int cartId, Product product) {

    }

    @Override
    public void removeProductFromCart(int cartId, Product product) {

    }

    @Override
    public void removeAllFromCart(int cartId) {

    }

    @Override
    public BigDecimal getTotalSum(int cartId) {
        return null;
    }
}
