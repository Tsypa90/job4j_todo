package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Account;

import java.util.Optional;
import java.util.function.Function;

@Repository
@ThreadSafe
public class AccountStore {
    private final SessionFactory sf;
    private final StoreWrapper wrapper;

    public AccountStore(SessionFactory sf, StoreWrapper wrapper) {
        this.sf = sf;
        this.wrapper = wrapper;
    }

    public Optional<Account> add(Account account) {
        return wrapper.tx(session -> {
            Optional<Account> rsl = session.createQuery("from Account s where s.login = :fLogin")
                    .setParameter("fLogin", account.getLogin()).uniqueResultOptional();
            if (rsl.isEmpty()) {
                session.save(account);
            }
            return rsl;
        }, sf);
    }

    public Optional<Account> getByLogin(String login, String password) {
        return wrapper.tx(session -> session.createQuery("from Account s where s.login = :fLogin and s.password = :fPassword")
                 .setParameter("fLogin", login)
                 .setParameter("fPassword", password).uniqueResultOptional(), sf);
    }
}
