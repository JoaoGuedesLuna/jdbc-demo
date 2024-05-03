package dev.guedes.jdbcdemo.repository;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Product;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * @author João Guedes
 */
public interface ProductRepository {

    void save(Product product) throws DatabaseException;

    Optional<Product> findById(Long id) throws DatabaseException;

    List<Product> findAll() throws DatabaseException;

    List<Product> findAllByNameContaining(String name) throws DatabaseException;

    List<Product> findAllByNameStartingWithAndPriceBetween(String name, BigDecimal minPrice, BigDecimal maxPrice) throws DatabaseException;

    void delete(Product product) throws DatabaseException;

}
