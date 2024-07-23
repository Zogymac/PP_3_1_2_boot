package ru.alex.Boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.Boot.model.User;
import ru.alex.Boot.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User findUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Transactional
    @Override
    public void saveOrUpdateUser(User user) {
        if (user.getId() != null) {
            User existingUser = findUserById(user.getId());

            updateFieldIfNotNull(existingUser::setName, user.getName(), existingUser.getName());
            updateFieldIfNotNull(existingUser::setEmail, user.getEmail(), existingUser.getEmail());
            updateFieldIfNotNull(existingUser::setRole, user.getRole(), existingUser.getRole());
            if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
            }

            userRepository.save(existingUser);
        } else {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Transactional
    @Override
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name).orElse(null);
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return userOptional.get();
    }

    private <T> void updateFieldIfNotNull(Consumer<T> setter, T newValue, T existingValue) {
        if (newValue != null && !newValue.equals(existingValue)) {
            setter.accept(newValue);
        }
    }

}
