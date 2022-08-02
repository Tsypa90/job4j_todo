package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Item;

import java.util.List;

@Repository
@ThreadSafe
public class ItemStore {
    private final SessionFactory sf;

    public ItemStore(SessionFactory sf) {
        this.sf = sf;
    }

    public Item add(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        session.getTransaction().commit();
        session.close();
        return item;
    }

    public boolean update(int id, Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        int result = session.createQuery("update Item s set s.name = :fName, s.description = :fDesc, s.done = :fDone where s.id = :fId")
                .setParameter("fName", item.getName())
                .setParameter("fDesc", item.getDescription())
                .setParameter("fDone", item.isDone())
                .setParameter("fId", id).executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result == 1;
    }

    public void updateDone(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        int rsl = session.createQuery("update Item s set s.done = true where s.id = :fId")
                .setParameter("fId", id).executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    public boolean delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        int result = session.createQuery("delete Item s where s.id = :fId")
                .setParameter("fId", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result == 1;
    }

    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        var rsl = session.createQuery("from Item order by created ASC ").list();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public List<Item> findAllNew() {
        Session session = sf.openSession();
        session.beginTransaction();
        var rsl = session.createQuery("from Item c where c.done = false order by created ASC ").list();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public List<Item> findAllDone() {
        Session session = sf.openSession();
        session.beginTransaction();
        var rsl = session.createQuery("from Item c where c.done = true order by created ASC ").list();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }

    public Item findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Item rsl = (Item) session.createQuery("from Item s where s.id = :fId")
                .setParameter("fId", id).uniqueResult();
        session.getTransaction().commit();
        session.close();
        return rsl;
    }
}
