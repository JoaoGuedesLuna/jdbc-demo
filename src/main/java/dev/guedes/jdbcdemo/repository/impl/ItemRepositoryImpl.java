package dev.guedes.jdbcdemo.repository.impl;

import dev.guedes.jdbcdemo.database.Database;
import dev.guedes.jdbcdemo.service.ServiceFactory;
import dev.guedes.jdbcdemo.repository.ItemRepository;
import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Item;
import lombok.Cleanup;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
public class ItemRepositoryImpl implements ItemRepository {

    @Override
    public void save(Item item) throws DatabaseException {
        if (item.getId() == null) {
            this.insert(item);
        }
    }

    private void insert(Item item) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "INSERT INTO items (quantity, unit_price, product_id, order_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setInt(1, item.getQuantity());
            statement.setBigDecimal(2, item.getUnitPrice());
            statement.setLong(3, item.getProduct().getId());
            statement.setLong(4, item.getOrder().getId());
            if (statement.executeUpdate() > 0) {
                @Cleanup ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    item.setId(resultSet.getLong(1));
                }
            }
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    @Override
    public Optional<Item> findById(Long id) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "SELECT * FROM items WHERE id = ?";
        Optional<Item> itemOptional = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            @ Cleanup ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                itemOptional = Optional.of(this.getResultSetItem(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return itemOptional;
    }

    @Override
    public List<Item> findAllByOrderId(Long orderId) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "SELECT * FROM items WHERE order_id = ?";
        List<Item> items = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, orderId);
            @Cleanup ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                items.add(this.getResultSetItem(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return items;
    }

    @Override
    public void delete(Item item) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "DELETE FROM items WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setLong(1,item.getId());
            statement.executeUpdate();
            connection.commit();
        } catch (SQLException e){
            try {
                connection.rollback();
            } catch (SQLException ex) {
                throw new DatabaseException(ex.getMessage());
            }
            throw new DatabaseException(e.getMessage());
        }
    }

    private Item getResultSetItem(ResultSet resultSet) throws SQLException, DatabaseException {
        return Item.builder()
                .id(resultSet.getLong("id"))
                .quantity(resultSet.getInt("quantity"))
                .unitPrice(resultSet.getBigDecimal("unit_price"))
                .product(ServiceFactory.createProductService()
                        .findById(resultSet.getLong("product_id"))
                        .orElseThrow()
                )
                .order(ServiceFactory.createOrderService()
                        .findById(resultSet.getLong("order_id"))
                        .orElseThrow(SQLException::new)
                )
                .build();
    }

}
