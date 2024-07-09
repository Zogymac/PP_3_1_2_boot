package ru.alex.Boot.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ru.alex.Boot.model.User;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MyUserDetails implements UserDetails {

    private final User user;

    public MyUserDetails(User user) {
        this.user = user;
    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = null;
        list.add(new SimpleGrantedAuthority(user.getRole())); // TODO: Сделать for по всем элементам который заполняет список ролей
//        list.add(new SimpleGrantedAuthority(user.getRole()));
        return Collections.singletonList(new SimpleGrantedAuthority(user.getRole()));
//        return list;
    }

    @Override
    public String getPassword() {
        return this.user.getPassword();
    }

    @Override
    public String getUsername() {
        return this.user.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Аккаунт активен(не просрочен)
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public User getUser() {
        return this.user;
    }
}
