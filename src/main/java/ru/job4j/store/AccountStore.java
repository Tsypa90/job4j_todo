package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Account;

import java.util.Optional;

@Repository
@ThreadSafe
public class AccountStore {
    private final SessionFactory sf;

    public AccountStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Optional<Account> add(Account account) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<Account> rsl = session.createQuery("from Account s where s.login = :fLogin")
                .setParameter("fLogin", account.getLogin()).uniqueResultOptional();
        if (rsl.isEmpty()) {
            session.save(account);
        }
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public Optional<Account> getByLogin(String login, String password) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<Account> rsl = session.createQuery("from Account s where s.login = :fLogin and s.password = :fPassword")
                 .setParameter("fLogin", login)
                 .setParameter("fPassword", password).uniqueResultOptional();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }
}
