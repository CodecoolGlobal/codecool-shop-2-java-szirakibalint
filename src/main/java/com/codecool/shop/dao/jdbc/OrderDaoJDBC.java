package com.codecool.shop.dao.jdbc;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.model.InvalidOrder;
import com.codecool.shop.model.Order;
import com.codecool.shop.model.ValidOrder;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderDaoJDBC implements OrderDao {

    private final DataSource dataSource;
    private static OrderDao instance;

    private OrderDaoJDBC(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static OrderDao getInstance(DataSource dataSource) {
        if (instance == null) {
            instance = new OrderDaoJDBC(dataSource);
        }
        return instance;
    }

    public static OrderDao getInstance() {
        return instance;
    }

    @Override
    public void add(Order order) {
        Map<String, String> data = order.getRelevantInformation();
        String firstSql;
        String secondSql;
        PreparedStatement firstStatement;
        int id;
        try (Connection conn = dataSource.getConnection()) {
            if (data.get("valid").equals("true")) {
                firstSql = "INSERT INTO valid_order (first_name, last_name, country, city, address, user_id) VALUES (?, ?, ?, ?, ?, ?)";
                firstStatement = conn.prepareStatement(firstSql, Statement.RETURN_GENERATED_KEYS);
                firstStatement.setString(1, data.get("first_name"));
                firstStatement.setString(2, data.get("last_name"));
                firstStatement.setString(3, data.get("country"));
                firstStatement.setString(4, data.get("city"));
                firstStatement.setString(5, data.get("address"));
                firstStatement.setInt(6, Integer.parseInt(data.get("user_id")));
                firstStatement.executeUpdate();
                secondSql = "INSERT INTO public.order (valid, cart_id, cart_data, valid_id) VALUES (true, ?, ?, ?)";
            } else {
                firstSql = "INSERT INTO invalid_order (message) VALUES (?)";
                firstStatement = conn.prepareStatement(firstSql, Statement.RETURN_GENERATED_KEYS);
                firstStatement.setString(1, data.get("message"));
                firstStatement.executeUpdate();
                secondSql = "INSERT INTO public.order (valid, cart_id, cart_data, invalid_id) VALUES (false, ?, ?, ?)";
            }
            ResultSet keys = firstStatement.getGeneratedKeys();
            id = keys.getInt(1);
            PreparedStatement secondStatement = conn.prepareStatement(secondSql);
            secondStatement.setInt(1, Integer.parseInt(data.get("cart_id")));
            secondStatement.setString(2, data.get("cart_data"));
            secondStatement.setInt(2, id);
            secondStatement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error while adding order");
        }
    }

    @Override
    public Order find(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sqlFirst = "SELECT valid, " +
                    "cart_data, " +
                    "cart_id, " +
                    "valid_id, " +
                    "invalid_id " +
                    "FROM public.order " +
                    "WHERE id = ?";
            PreparedStatement statementFirst = conn.prepareStatement(sqlFirst);
            statementFirst.setInt(1, id);
            ResultSet resultSet = statementFirst.executeQuery();
            if (resultSet.next()) {
                return createOrderFromResultSet(resultSet, statementFirst, conn, id);
            }
        } catch (SQLException e) {
            System.out.println("Error while finding order");
        }
        return null;
    }

    @Override
    public void remove(int id) {
        try (Connection conn = dataSource.getConnection()) {
            String sqlSelect = "SELECT valid, valid_id, invalid_id FROM public.order WHERE id = ?";
            PreparedStatement statementSelect = conn.prepareStatement(sqlSelect, Statement.RETURN_GENERATED_KEYS);
            statementSelect.setInt(1, id);
            ResultSet resultSet = statementSelect.executeQuery();
            if (resultSet.next()) {
                String sqlDeleteFirst = "DELETE FROM public.order WHERE id = ?";
                PreparedStatement statementDeleteFirst = conn.prepareStatement(sqlDeleteFirst);
                statementDeleteFirst.setInt(1, id);
                statementDeleteFirst.executeUpdate();
                if (resultSet.getBoolean("valid")) {
                    String sqlDeleteSecond = "DELETE FROM valid_order WHERE id = ?";
                    PreparedStatement statementDeleteSecond = conn.prepareStatement(sqlDeleteSecond);
                    statementDeleteSecond.setInt(1, resultSet.getInt("valid_id"));
                    statementDeleteSecond.executeUpdate();
                } else {
                    String sqlDeleteSecond = "DELETE FROM invalid_order WHERE id = ?";
                    PreparedStatement statementDeleteSecond = conn.prepareStatement(sqlDeleteSecond);
                    statementDeleteSecond.setInt(1, resultSet.getInt("invalid_id"));
                    statementDeleteSecond.executeUpdate();
                }
            }
        } catch (SQLException e) {
            System.out.println("Error while deleting order");
        }
    }

    @Override
    public List<Order> getAll() {
        try (Connection conn = dataSource.getConnection()) {
            String sqlFirst = "SELECT id, " +
                    "valid, " +
                    "cart_data, " +
                    "cart_id, " +
                    "valid_id, " +
                    "invalid_id " +
                    "FROM public.order";
            PreparedStatement statementFirst = conn.prepareStatement(sqlFirst);
            ResultSet resultSet = statementFirst.executeQuery();
            List<Order> orders = new ArrayList<>();
            while (resultSet.next()) {
                Order newOrder = createOrderFromResultSet(resultSet, statementFirst, conn, resultSet.getInt("id"));
                orders.add(newOrder);
            }
            return orders;
        } catch (SQLException e) {
            System.out.println("Error while getting all orders");
        }
        return null;
    }

    private Order createOrderFromResultSet(ResultSet resultSet, PreparedStatement statementFirst, Connection conn, int id) throws SQLException {
        String cart = resultSet.getString("cart_data");
        int cartId = resultSet.getInt("cart_id");
        boolean valid = resultSet.getBoolean("valid");
        if (valid) {
            String sqlSecond = "SELECT first_name, " +
                    "last_name, " +
                    "country, " +
                    "city, " +
                    "address, " +
                    "user_id " +
                    "FROM valid_order " +
                    "WHERE id = ?";
            PreparedStatement statementSecond = conn.prepareStatement(sqlSecond);
            statementFirst.setInt(1, resultSet.getInt("valid_id"));
            resultSet = statementSecond.executeQuery();
            if (resultSet.next()) {
                Order order = new ValidOrder(resultSet.getString("firstname"),
                        resultSet.getString("last_name"),
                        resultSet.getString("country"),
                        resultSet.getString("city"),
                        resultSet.getString("address"),
                        resultSet.getInt("user_id"),
                        cartId,
                        cart);
                order.setId(id);
                return order;
            }
        } else {
            String sqlSecond = "SELECT message " +
                    "FROM invalid_order " +
                    "WHERE id = ?";
            PreparedStatement statementSecond = conn.prepareStatement(sqlSecond);
            statementFirst.setInt(1, resultSet.getInt("invalid_id"));
            resultSet = statementSecond.executeQuery();
            if (resultSet.next()) {
                Order order = new InvalidOrder(resultSet.getString("message"), cartId, cart);
                order.setId(id);
                return order;
            }
        }
        return null;
    }
}
