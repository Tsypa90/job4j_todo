package ru.job4j.store;

import net.jcip.annotations.ThreadSafe;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.model.Category;

import java.util.List;

@Repository
@ThreadSafe
public class CategoryStore implements Store {
    private final SessionFactory sf;

    public CategoryStore(SessionFactory sf) {
        this.sf = sf;
    }

    public List<Category> findAll() {
        return tx(session -> session.createQuery("from Category order by id asc").list(), sf);
    }

    public Category findById(int id) {
        return (Category) tx(session ->
                session.createQuery("from Category c where c.id = :fId")
                        .setParameter("fId", id).uniqueResult(), sf);
    }
}
