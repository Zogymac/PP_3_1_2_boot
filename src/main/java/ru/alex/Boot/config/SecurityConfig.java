package ru.alex.Boot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import ru.alex.Boot.service.UserServiceDetailsImpl;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserServiceDetailsImpl userServiceDetails;
    private final LoginSuccesHandler loginSuccesHandler;

    @Autowired
    public SecurityConfig(UserServiceDetailsImpl userServiceDetails, LoginSuccesHandler loginSuccesHandler) {
        this.userServiceDetails = userServiceDetails;
        this.loginSuccesHandler = loginSuccesHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // конфигурируем сам security
        // конфирурием авторизацию
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/admin", "/user/edit/**").hasRole("ADMIN") // Тут не надо ROLE_
                .antMatchers("/auth/login", "/auth/registration", "/error").permitAll()
                .anyRequest().hasAnyRole("USER", "ADMIN")
//                .anyRequest().authenticated()
                .and()
                .formLogin().loginPage("/auth/login")
                .loginProcessingUrl("/process_login")
                .successHandler(loginSuccesHandler)
//                .defaultSuccessUrl("/user", true)
                .failureUrl("/auth/login?error")
                .and()
                .logout().logoutUrl("/logout").logoutSuccessUrl("/auth/login");

    }

    // Настраивает аутентификацию
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userServiceDetails)
                .passwordEncoder(getPasswordEncoder());
    }

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
