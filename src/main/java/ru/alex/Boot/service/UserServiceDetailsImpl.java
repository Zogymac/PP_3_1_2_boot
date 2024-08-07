package ru.alex.Boot.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.alex.Boot.model.User;
import ru.alex.Boot.repository.UserRepository;
import ru.alex.Boot.security.MyUserDetails;

import java.util.Optional;

@Service
public class UserServiceDetailsImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserServiceDetailsImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByName(username);
        if (userOptional.isEmpty()) {
            throw new UsernameNotFoundException("User not found!");
        }

        return new MyUserDetails(userOptional.get());
    }

/*    private Collection<? extends GrantedAuthority> mapRolesToAuthorites(Collection<Role> roles) {
        return roles.stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .collect(Collectors.toList());
    }*/
}
