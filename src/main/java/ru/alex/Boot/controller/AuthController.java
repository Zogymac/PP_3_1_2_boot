package ru.alex.Boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.alex.Boot.model.User;
import ru.alex.Boot.service.RegistrationService;
import ru.alex.Boot.service.UserService;
import ru.alex.Boot.util.UserValidator;

import javax.validation.Valid;


@Controller
@RequestMapping("/auth")
public class AuthController {

    private final RegistrationService registrationService;
    private final UserValidator userValidator;
    private final UserService userService;

    @Autowired
    public AuthController(RegistrationService registrationService, UserValidator userValidator, UserService userService) {
        this.registrationService = registrationService;
        this.userValidator = userValidator;
        this.userService = userService;
    }

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }

/*    @GetMapping("/registration")
    public String registrationPage(@ModelAttribute("user") User user) {


        return "auth/registration";
    }*/

    @GetMapping("/registration")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", userService.getAllRoles());

        return "auth/registration";
    }



    @PostMapping("/registration")
    public String performRegistration(@ModelAttribute("user") @Valid User user,
                                      BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "/auth/registration";
        }

        registrationService.register(user);

        return "redirect:/auth/login";
    }
}
