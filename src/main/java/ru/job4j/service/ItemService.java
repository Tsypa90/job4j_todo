package ru.job4j.service;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Service;
import ru.job4j.model.Category;
import ru.job4j.model.Item;
import ru.job4j.store.CategoryStore;
import ru.job4j.store.ItemStore;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Service
@ThreadSafe
public class ItemService {
    private final ItemStore store;
    private final CategoryStore categoryStore;

    public ItemService(ItemStore store, CategoryStore categoryStore) {
        this.store = store;
        this.categoryStore = categoryStore;
    }

    public Item add(Item item) {
        item.setCreated(new Date(System.currentTimeMillis()));
        item.setDone(false);
        return store.add(item);
    }

    public boolean update(int id, Item item) {
        return store.update(id, item);
    }

    public boolean delete(int id) {
        return store.delete(id);
    }

    public List<Item> findAll() {
        return store.findAll();
    }

    public List<Item> findAllNew() {
        return store.findAllNew();
    }

    public List<Item> findAllDone() {
        return store.findAllDone();
    }

    public Item findById(int id) {
        return store.findById(id);
    }

    public void isDoneItem(int id) {
        store.updateDone(id);
    }

    public List<Category> findAllCategories() {
        return categoryStore.findAll();
    }

    public Category findCategoryById(int id) {
        return categoryStore.findById(id);
    }
}
