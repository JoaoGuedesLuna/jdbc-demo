package dev.guedes.jdbcdemo.repository.impl;

import dev.guedes.jdbcdemo.database.Database;
import dev.guedes.jdbcdemo.service.ServiceFactory;
import dev.guedes.jdbcdemo.repository.OrderRepository;
import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Order;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
public class OrderRepositoryImpl implements OrderRepository {

    @Override
    public void save(Order order) throws DatabaseException {
        if (order.getId() == null) {
            this.insert(order);
        }
    }

    private void insert(Order order) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "INSERT INTO orders (account_id) VALUES (?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setLong(1, order.getAccount().getId());
            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    order.setId(resultSet.getLong(1));
                }
                resultSet.close();
            }
            connection.commit();
            connection.close();
        }
        catch (SQLException e) {
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public Optional<Order> findById(Long id) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM orders WHERE id = ?";
        Optional<Order> optionalOrder = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                optionalOrder = Optional.of(this.getResultSetOrder(resultSet));
            }
            resultSet.close();
            connection.close();
        }
        catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
        return optionalOrder;
    }

    @Override
    public List<Order> findAllByAccountId(Long accountId) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM orders WHERE account_id = ?";
        List<Order> orders = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, accountId);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                orders.add(this.getResultSetOrder(resultSet));
            }
            resultSet.close();
            connection.close();
        }
        catch (SQLException e) {
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
        return orders;
    }

    @Override
    public void delete(Order order) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "DELETE FROM orders WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setLong(1,order.getId());
            statement.executeUpdate();
            connection.commit();
            connection.close();
        }
        catch (SQLException e){
            try {
                connection.rollback();
                connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public BigDecimal callOrderTotalPrice(Order order) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "CALL orderTotalPrice(?)";
        BigDecimal totalPrice = new BigDecimal("0.0");
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, order.getId());
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                BigDecimal result = resultSet.getBigDecimal(1);
                if (result != null) {
                    totalPrice = result;
                }
            }
            resultSet.close();
            connection.close();
            return totalPrice;
        }
        catch (SQLException e){
            try {
                connection.close();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    private Order getResultSetOrder(ResultSet resultSet) throws SQLException, DatabaseException {
        return Order.builder()
                .id(resultSet.getLong("id"))
                .date(resultSet.getTimestamp("order_date").toLocalDateTime())
                .account(ServiceFactory.createAccountService()
                        .findById(resultSet.getLong("account_id"))
                        .orElseThrow()
                )
                .build();
    }

}
