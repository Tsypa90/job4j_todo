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

    public AccountStore(SessionFactory sf) {
        this.sf = sf;
    }

    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }

    public Optional<Account> add(Account account) {
        return this.tx(session -> {
            Optional<Account> rsl = session.createQuery("from Account s where s.login = :fLogin")
                    .setParameter("fLogin", account.getLogin()).uniqueResultOptional();
            if (rsl.isEmpty()) {
                session.save(account);
            }
            return rsl;
        });
    }

    public Optional<Account> getByLogin(String login, String password) {
        return this.tx(session -> session.createQuery("from Account s where s.login = :fLogin and s.password = :fPassword")
                 .setParameter("fLogin", login)
                 .setParameter("fPassword", password).uniqueResultOptional());
    }
}
