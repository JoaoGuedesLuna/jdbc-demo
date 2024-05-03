package dev.guedes.jdbcdemo.repository;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Account;
import java.util.Optional;

/**
 * @author João Guedes
 */
public interface AccountRepository {

    void save(Account account) throws DatabaseException;

    Optional<Account> findById(Long id) throws DatabaseException;

    Optional<Account> findByEmail(String email) throws DatabaseException;

    void delete(Account account) throws DatabaseException;

}
