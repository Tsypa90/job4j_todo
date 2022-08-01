package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.model.Account;
import ru.job4j.store.AccountStore;

import java.util.Optional;

@Service
@ThreadSafe
public class AccountService {
    private final AccountStore store;

    public AccountService(AccountStore store) {
        this.store = store;
    }

    public Optional<Account> add(Account account) {
        return store.add(account);
    }

    public Optional<Account> getByLogin(String login) {
        return store.getByLogin(login);
    }
}
