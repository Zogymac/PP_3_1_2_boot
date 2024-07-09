package ru.alex.Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alex.Boot.model.User;
import ru.alex.Boot.service.UserServiceDetailsImpl;

@Component
public class UserValidator implements Validator {

    private final UserServiceDetailsImpl userServiceDetails;

    @Autowired
    public UserValidator(UserServiceDetailsImpl userServiceDetails) {
        this.userServiceDetails = userServiceDetails;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try {
            userServiceDetails.loadUserByUsername(user.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("name", "", "Человек с таким именем пользователя существует");
    }
}
