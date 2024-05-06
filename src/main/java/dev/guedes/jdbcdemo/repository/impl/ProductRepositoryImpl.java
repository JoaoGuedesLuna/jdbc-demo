package dev.guedes.jdbcdemo.repository.impl;

import dev.guedes.jdbcdemo.database.Database;
import dev.guedes.jdbcdemo.repository.ProductRepository;
import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Product;
import lombok.Cleanup;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
public class ProductRepositoryImpl implements ProductRepository {

    @Override
    public void save(Product product) throws DatabaseException {
        if (product.getId() == null) {
            this.insert(product);
        } else {
            this.update(product);
        }
    }

    private void insert(Product product) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "INSERT INTO products (name, description, price) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            if (statement.executeUpdate() > 0) {
                @Cleanup ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    product.setId(resultSet.getLong(1));
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

    private void update(Product product) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "UPDATE products SET name = ?, description = ?, price = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setLong(4, product.getId());
            statement.executeUpdate();
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
    public Optional<Product> findById(Long id) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "SELECT * FROM products WHERE id = ?";
        Optional<Product> optionalProduct = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            @Cleanup ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                optionalProduct = Optional.of(this.getResultSetProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return optionalProduct;
    }

    @Override
    public List<Product> findAll() throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "SELECT * FROM products";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            @Cleanup ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(this.getResultSetProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> findAllByNameContaining(String name) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "SELECT * FROM products WHERE name LIKE CONCAT('%', ?, '%')";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            @Cleanup ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(this.getResultSetProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return products;
    }

    @Override
    public List<Product> findAllByNameStartingWithAndPriceBetween(String name, BigDecimal minPrice, BigDecimal maxPrice) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "SELECT * FROM products WHERE name LIKE CONCAT(?, '%') AND price BETWEEN ? AND ?";
        List<Product> products = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, name);
            statement.setBigDecimal(2, minPrice);
            statement.setBigDecimal(3, maxPrice);
            @Cleanup ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                products.add(this.getResultSetProduct(resultSet));
            }
        } catch (SQLException e) {
            throw new DatabaseException(e.getMessage());
        }
        return products;
    }

    @Override
    public void delete(Product product) throws DatabaseException {
        @Cleanup Connection connection = Database.getConnection();
        String query = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setLong(1,product.getId());
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

    private Product getResultSetProduct(ResultSet resultSet) throws SQLException {
        return Product.builder()
                .id(resultSet.getLong("id"))
                .name(resultSet.getString("name"))
                .description(resultSet.getString("description"))
                .price(resultSet.getBigDecimal("price"))
                .build();
    }

}
