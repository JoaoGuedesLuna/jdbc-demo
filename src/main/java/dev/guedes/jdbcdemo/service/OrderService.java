package dev.guedes.jdbcdemo.service;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Order;
import dev.guedes.jdbcdemo.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;

    public void save(Order order) throws DatabaseException {
        this.orderRepository.save(order);
    }

    public Optional<Order> findById(Long id) throws DatabaseException {
        return this.orderRepository.findById(id);
    }

    public List<Order> findAllByAccountId(Long accountId) throws DatabaseException {
        return this.orderRepository.findAllByAccountId(accountId);
    }

    public void delete(Order order) throws DatabaseException {
        this.orderRepository.delete(order);
    }

    public BigDecimal callOrderTotalPrice(Order order) throws DatabaseException {
        return this.orderRepository.callOrderTotalPrice(order);
    }

}
