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
                               @RequestParam(name = "fail", required = false) Boolean fail) {
        Account account = (Account) session.getAttribute("account");
        model.addAttribute("account", account);
        model.addAttribute("fail", fail != null);
        return "registration";
    }

    @GetMapping("/login")
    public String login(Model model,
                       @RequestParam(name = "fail", required = false) Boolean fail) {
        model.addAttribute("fail", fail != null);
        return "login";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute Account account) {
        Optional<Account> regAccount = service.add(account);
        if (regAccount.isEmpty()) {
            return "redirect:/registration?fail=true";
        }
        return "redirect:/login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Account account, HttpServletRequest request) {
        Optional<Account> accountDb = service.getByLogin(account.getLogin(), account.getPassword());
        if (accountDb.isEmpty()) {
            return "redirect:/login?fail=true";
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