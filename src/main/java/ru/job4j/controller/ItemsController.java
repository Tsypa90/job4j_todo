package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Item;
import ru.job4j.service.ItemService;

@Controller
@ThreadSafe
public class ItemsController {
    private final ItemService service;

    public ItemsController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/items")
    public String items(Model model) {
        model.addAttribute("items", service.findAll());
        return "items";
    }

    @GetMapping("/addItem")
    public String addItem() {
        return "/addItem";
    }

    @GetMapping("/newItems")
    public String newItems(Model model) {
        model.addAttribute("newItems", service.findAllNew());
        return "newItems";
    }

    @GetMapping("/doneItems")
    public String doneItems(Model model) {
        model.addAttribute("doneItems", service.findAllDone());
        return "doneItems";
    }

    @GetMapping("/items/{item.id}")
    public String getItem(Model model, @PathVariable("item.id") int id) {
        model.addAttribute("item", service.findById(id));
        return "item";
    }

    @GetMapping("/update/{item.id}")
    public String updateItem(Model model, @PathVariable("item.id") int id) {
        model.addAttribute("item", service.findById(id));
        return "update";
    }

    @PostMapping("/createItem")
    public String createItem(@ModelAttribute Item item) {
        service.add(item);
        return "redirect:/items";
    }

    @PostMapping("/delete/{item.id}")
    public String deleteItem(@PathVariable("item.id") int id) {
        service.delete(id);
        return "redirect:/items";
    }

    @PostMapping("/isDone/{item.id}")
    public String doneItem(@PathVariable("item.id") int id) {
        service.isDoneItem(id);
        return "redirect:/items";
    }

    @PostMapping("update")
    public String update(@ModelAttribute Item item) {
        System.out.println(item.getId());
        service.update(item.getId(), item);
        return "redirect:/items";
    }
}
