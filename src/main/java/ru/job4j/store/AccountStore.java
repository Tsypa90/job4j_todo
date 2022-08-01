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
        Account rsl = (Account) session.createQuery("from Account s where s.login = :fLogin")
                .setParameter("fLogin", account.getLogin()).uniqueResult();
        if (rsl == null) {
            session.save(account);
        }
        session.getTransaction().commit();
        session.close();
        return account.getId() == 0 ? Optional.empty() : Optional.of(account);
    }

    public Optional<Account> getByLogin(String login) {
        Session session = sf.openSession();
        session.beginTransaction();
        Account rsl = (Account) session.createQuery("from Account s where s.login = :fLogin")
                 .setParameter("fLogin", login).uniqueResult();
        if (rsl == null) {
            return Optional.empty();
        }
        session.getTransaction().commit();
        session.close();
        return Optional.of(rsl);
    }
}
