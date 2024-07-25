package ru.alex.Boot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.alex.Boot.model.Role;
import ru.alex.Boot.model.User;
import ru.alex.Boot.service.RoleService;
import ru.alex.Boot.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class UserRestController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public UserRestController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> listUsers(@ModelAttribute("user") User user, Model model, Principal principal) {
        List<User> response = userService.getAllUsers();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> listRoles(@ModelAttribute("user") User user, Model model, Principal principal) {
        List<Role> response = roleService.getAllRoles();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User response = userService.findUserById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/admin/edit")
    public ResponseEntity<HttpStatus> saveUser(@RequestBody User user) {
        userService.saveOrUpdateUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/admin/delete/{id}")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        userService.deleteUser(id);
        return ResponseEntity.ok(HttpStatus.OK);
    }



}
