package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Item;

import java.util.List;
import java.util.function.Function;

@Repository
@ThreadSafe
public class ItemStore {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
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

    public Item add(Item item) {
        return this.tx(session -> {
            session.save(item);
            return item;
        });
    }

    public boolean update(int id, Item item) {
        return this.tx(session -> session.createQuery("update Item s set s.name = :fName, s.description = :fDesc, s.done = :fDone where s.id = :fId")
                .setParameter("fName", item.getName())
                .setParameter("fDesc", item.getDescription())
                .setParameter("fDone", item.isDone())
                .setParameter("fId", id).executeUpdate()) == 1;
    }

    public void updateDone(int id) {
        this.tx(session -> session.createQuery("update Item s set s.done = true where s.id = :fId")
                .setParameter("fId", id).executeUpdate());
    }

    public boolean delete(int id) {
        return this.tx(session -> session.createQuery("delete Item s where s.id = :fId")
                .setParameter("fId", id)
                .executeUpdate()) == 1;
    }

    public List<Item> findAll() {
        return this.tx(session -> session.createQuery("from Item order by created ASC ").list());
    }

    public List<Item> findAllNew() {
        return this.tx(session -> session
                .createQuery("from Item c where c.done = false order by created ASC ").list());
    }

    public List<Item> findAllDone() {
        return this.tx(session -> session
                .createQuery("from Item c where c.done = true order by created ASC ").list());
    }

    public Item findById(int id) {
        return (Item) this.tx(session -> session.createQuery("from Item s where s.id = :fId")
                .setParameter("fId", id).uniqueResult());
    }
}
