package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Item;

import java.util.List;

@Repository
@ThreadSafe
public class ItemStore implements Store {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Item add(Item item) {
        return tx(session -> {
            session.save(item);
            return item;
        }, sf);
    }

    public boolean update(int id, Item item) {
        return tx(session -> session.createQuery("update Item s set s.name = :fName, s.description = :fDesc, s.done = :fDone where s.id = :fId")
                .setParameter("fName", item.getName())
                .setParameter("fDesc", item.getDescription())
                .setParameter("fDone", item.isDone())
                .setParameter("fId", id).executeUpdate(), sf) == 1;
    }

    public void updateDone(int id) {
        tx(session -> session.createQuery("update Item s set s.done = true where s.id = :fId")
                .setParameter("fId", id).executeUpdate(), sf);
    }

    public boolean delete(int id) {
        return tx(session -> session.createQuery("delete Item s where s.id = :fId")
                .setParameter("fId", id)
                .executeUpdate(), sf) == 1;
    }

    public List<Item> findAll() {
        return tx(session -> session.createQuery("select distinct c from Item c join fetch c.categories order by c.id ASC").list(), sf);
    }

    public List<Item> findAllNew() {
        return tx(session -> session
                .createQuery("from Item c where c.done = false order by created ASC ").list(), sf);
    }

    public List<Item> findAllDone() {
        return tx(session -> session
                .createQuery("from Item c where c.done = true order by created ASC ").list(), sf);
    }

    public Item findById(int id) {
        return (Item) tx(session -> session.createQuery("from Item s where s.id = :fId")
                .setParameter("fId", id).uniqueResult(), sf);
    }
}
