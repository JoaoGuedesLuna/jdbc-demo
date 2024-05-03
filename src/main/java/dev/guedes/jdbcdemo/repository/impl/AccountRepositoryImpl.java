package dev.guedes.jdbcdemo.repository.impl;

import dev.guedes.jdbcdemo.database.Database;
import dev.guedes.jdbcdemo.repository.AccountRepository;
import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Account;
import java.sql.*;
import java.util.Optional;

/**
 * @author João Guedes
 */
public class AccountRepositoryImpl implements AccountRepository {

    @Override
    public void save(Account account) throws DatabaseException {
        if (account.getId() == null) {
            this.insert(account);
        }
        else {
            this.update(account);
        }
    }

    private void insert(Account account) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "INSERT INTO accounts (email, password) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            connection.setAutoCommit(false);
            statement.setString(1, account.getEmail());
            statement.setString(2, account.getPassword());
            if (statement.executeUpdate() > 0) {
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    account.setId(resultSet.getLong(1));
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

    private void update(Account account) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "UPDATE accounts SET email = ?, password = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setString(1, account.getEmail());
            statement.setString(2, account.getPassword());
            statement.setLong(3, account.getId());
            statement.executeUpdate();
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
    public Optional<Account> findById(Long id) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM accounts WHERE id = ?";
        Optional<Account> optionalAccount = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                optionalAccount = Optional.of(this.getResultSetAccount(resultSet));
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
        return optionalAccount;
    }

    @Override
    public Optional<Account> findByEmail(String email) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "SELECT * FROM accounts WHERE email = ?";
        Optional<Account> optionalAccount = Optional.empty();
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                optionalAccount = Optional.of(this.getResultSetAccount(resultSet));
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
        return optionalAccount;
    }

    @Override
    public void delete(Account account) throws DatabaseException {
        Connection connection = Database.getConnection();
        String query = "DELETE FROM accounts WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            connection.setAutoCommit(false);
            statement.setLong(1,account.getId());
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

    private Account getResultSetAccount(ResultSet resultSet) throws SQLException {
        return Account.builder()
                .id(resultSet.getLong("id"))
                .email(resultSet.getString("email"))
                .password(resultSet.getString("password"))
                .build();
    }

}
