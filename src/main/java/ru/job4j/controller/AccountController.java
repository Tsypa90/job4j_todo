package ru.job4j.controller;

import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.job4j.model.Account;
import ru.job4j.service.AccountService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@ThreadSafe
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService service) {
        this.service = service;
    }

    @GetMapping("/registration")
    public String registration(Model model, HttpSession session,
                               @RequestParam(name = "fail", required = false) Boolean fail,
                               @RequestParam(name = "length", required = false) Boolean length) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("fail", fail != null);
        model.addAttribute("length", length != null);
        return "registration";
    }

    @GetMapping("/login")
    public String login(Model model,
                       @RequestParam(name = "fail", required = false) Boolean fail,
                        @RequestParam(name = "password", required = false) Boolean password) {
        model.addAttribute("fail", fail != null);
        model.addAttribute("password", password != null);
        return "login";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute Account account) {
        if (account.getName().isBlank()) {
            return "redirect:/registration?length=true";
        } else if (account.getLogin().isBlank()) {
            return "redirect:/registration?length=true";
        } else if (account.getPassword().isBlank()) {
            return "redirect:/registration?length=true";
        }
        Optional<Account> regAccount = service.add(account);
        if (regAccount.isEmpty()) {
            return "redirect:/registration?fail=true";
        }
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Account account, HttpServletRequest request) {
        Optional<Account> accountDb = service.getByLogin(account.getLogin());
        if (accountDb.isEmpty()) {
            return "redirect:/login?fail=true";
        }
        if (!account.getPassword().equals(accountDb.get().getPassword())) {
            return "redirect:/login?password=true";
        }
        HttpSession session = request.getSession();
        session.setAttribute("account", accountDb.get());
        return "redirect:/items";
    }

    @GetMapping("/logout")
    public String logOut(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}