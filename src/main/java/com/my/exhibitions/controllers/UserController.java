package com.my.exhibitions.controllers;

import com.my.exhibitions.entities.User;
import com.my.exhibitions.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;


@Controller
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String getLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String logUserIn(@RequestParam("login") String login,
                            @RequestParam("password") String password) {
        System.out.println(login + "\n" + password);
        return "redirect:/home";
    }

    @GetMapping("/registration")
    public String getRegistration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registerNewUser(@Valid @ModelAttribute("user")  User user, BindingResult bindingResult) {
        boolean alreadyExists = userService.existsByUsername(user.getUsername());
        if(alreadyExists) {
            bindingResult.rejectValue(
                    "username",
                    "",
                    "User with such username already exists"
            );
        }
        if(bindingResult.hasErrors()) {
            return "registration";
        }
        userService.save(user);
        return "redirect:/home";
    }
}