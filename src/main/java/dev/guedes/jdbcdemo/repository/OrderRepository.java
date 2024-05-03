package dev.guedes.jdbcdemo.repository;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Order;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
public interface OrderRepository {

    void save(Order order) throws DatabaseException;

    Optional<Order> findById(Long id) throws DatabaseException;

    List<Order> findAllByAccountId(Long accountId) throws DatabaseException;

    void delete(Order order) throws DatabaseException;

    BigDecimal callOrderTotalPrice(Order order) throws DatabaseException;

}
