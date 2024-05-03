package dev.guedes.jdbcdemo.service;

import dev.guedes.jdbcdemo.exception.DatabaseException;
import dev.guedes.jdbcdemo.model.Account;
import dev.guedes.jdbcdemo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.mindrot.jbcrypt.BCrypt;
import java.util.Optional;

/**
 * @author João Guedes
 */
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;

    public void save(Account account) throws DatabaseException {
        String hashedPassword = BCrypt.hashpw(account.getPassword(), BCrypt.gensalt());
        account.setPassword(hashedPassword);
        this.accountRepository.save(account);
    }

    public Optional<Account> findById(Long id) throws DatabaseException {
        return this.accountRepository.findById(id);
    }

    public Optional<Account> findByEmail(String email) throws DatabaseException {
        return this.accountRepository.findByEmail(email);
    }

    public void delete(Account account) throws DatabaseException {
        this.accountRepository.delete(account);
    }

}
