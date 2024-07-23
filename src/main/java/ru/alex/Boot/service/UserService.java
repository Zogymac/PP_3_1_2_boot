package ru.alex.Boot.service;

import ru.alex.Boot.model.User;

import java.util.List;

public interface UserService {
    User findUserById(Long id);

    void saveOrUpdateUser(User user);

    void deleteUser(Long id);

    List<User> getAllUsers();

    User findUserByName(String name);
}
