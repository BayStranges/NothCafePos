package com.nothcoffee.NothCoffeePOS.controller;

import com.nothcoffee.NothCoffeePOS.model.User;
import com.nothcoffee.NothCoffeePOS.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String showlogin(Model model) {
        model.addAttribute("error", "");
        return "login"; // Giriş sayfasını döndürüyoruz
    }

    @PostMapping("/login")
    public String login(@RequestParam String username, @RequestParam String password, Model model) {
        Optional<User> user = userService.findByUsername(username);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return "dashboard"; // Giriş başarılıysa panoya yönlendiriyoruz
        } else {
            model.addAttribute("error", "Giriş başarısız. Tekrar deneyin.");
            return "login"; // Giriş başarısızsa tekrar giriş sayfasına yönlendiriyoruz
        }
    }
}
