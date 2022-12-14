package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Account;

import java.util.Optional;

@Repository
@ThreadSafe
public class AccountStore implements Store {
    private final SessionFactory sf;

    public AccountStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<Account> add(Account account) {
        return tx(session -> {
            Optional<Account> rsl = session.createQuery("from Account s where s.login = :fLogin")
                    .setParameter("fLogin", account.getLogin()).uniqueResultOptional();
            if (rsl.isEmpty()) {
                session.save(account);
            }
            return rsl;
        }, sf);
    }

    public Optional<Account> getByLogin(String login, String password) {
        return tx(session -> session.createQuery("from Account s where s.login = :fLogin and s.password = :fPassword")
                 .setParameter("fLogin", login)
                 .setParameter("fPassword", password).uniqueResultOptional(), sf);
    }
}
