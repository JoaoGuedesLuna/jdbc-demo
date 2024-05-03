package dev.guedes.jdbcdemo.repository;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Item;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
public interface ItemRepository {

    void save(Item item) throws DatabaseException;

    Optional<Item> findById(Long id) throws DatabaseException;

    List<Item> findAllByOrderId(Long orderId) throws DatabaseException;

    void delete(Item item) throws DatabaseException;

}
