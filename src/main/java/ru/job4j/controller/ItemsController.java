package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.model.Account;
import ru.job4j.model.Item;
import ru.job4j.service.ItemService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@ThreadSafe
public class ItemsController {
    private final ItemService service;

    public ItemsController(ItemService service) {
        this.service = service;
    }

    @GetMapping("/items")
    public String items(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("items", service.findAll());
        return "items";
    }

    @GetMapping("/addItem")
    public String addItem(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("categories", service.findAllCategories());
        return "/addItem";
    }

    @GetMapping("/newItems")
    public String newItems(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("newItems", service.findAllNew());
        return "newItems";
    }

    @GetMapping("/doneItems")
    public String doneItems(Model model, HttpSession session) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("doneItems", service.findAllDone());
        return "doneItems";
    }

    @GetMapping("/items/{item.id}")
    public String getItem(Model model, HttpSession session, @PathVariable("item.id") int id) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("item", service.findById(id));
        return "item";
    }

    @GetMapping("/update/{item.id}")
    public String updateItem(Model model, HttpSession session, @PathVariable("item.id") int id) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("item", service.findById(id));
        return "update";
    }

    @PostMapping("/createItem")
    public String createItem(@RequestParam("category_id") List<Integer> categoryId, @ModelAttribute Item item, HttpSession session) {
        item.setAccount((Account) session.getAttribute("account"));
        for (Integer id : categoryId) {
            var category = service.findCategoryById(id);
            item.getCategories().add(category);
        }
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
