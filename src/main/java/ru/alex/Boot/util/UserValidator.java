package ru.alex.Boot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.alex.Boot.model.User;

@Component
public class UserValidator implements Validator {

    private final UserDetailsService userServiceImpl;

    @Autowired
    public UserValidator(UserDetailsService userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return User.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        User user = (User) target;

        try {
            userServiceImpl.loadUserByUsername(user.getName());
        } catch (UsernameNotFoundException ignored) {
            return;
        }

        errors.rejectValue("name", "", "Человек с таким именем пользователя существует");
    }
}
