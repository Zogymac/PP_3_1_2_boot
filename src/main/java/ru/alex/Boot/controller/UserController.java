package ru.alex.Boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alex.Boot.model.User;
import ru.alex.Boot.service.UserService;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "users";
    }

    @GetMapping("/add")
    public String showForm(Model model) {
        model.addAttribute("user", new User());
        return "user-form";
    }

    @GetMapping("/edit")
    public String showEditForm(@RequestParam("id") Long id, Model model) {
        User user = userService.findUserById(id);
        model.addAttribute("user", user);
        return "user-form";
    }

    @PostMapping
    public String saveUser(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/user";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/user";
    }
}
