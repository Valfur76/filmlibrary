package com.bagrov.springpmhw.videorent.config;

import com.bagrov.springpmhw.videorent.service.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.bagrov.springpmhw.videorent.constants.UserRolesConstants.ADMIN;
import static com.bagrov.springpmhw.videorent.constants.UserRolesConstants.MANAGER;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final CustomUserDetailsService customUserDetailsService;

    public WebSecurityConfig(BCryptPasswordEncoder bCryptPasswordEncoder, CustomUserDetailsService customUserDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.customUserDetailsService = customUserDetailsService;
    }

    private final String[] RESOURCES_WHITE_LIST = {
            "/resources/**",
            "/static/**",
            "/js/**",
            "/css/**",
            "/",
            "swagger-ui/**"
    };

    private final String[] FILMS_WHITE_LIST = {
            "/films"
    };

    private final String[] FILMS_PERMISSIONS_LIST = {
            "/films/addFilm"
    };

    private final String[] DIRECTORS_WHITE_LIST = {
            "/directors"
    };

    private final String[] DIRECTORS_PERMISSIONS_LIST = {
            "/directors/addDirector"
    };

    private final String[] USERS_WHITE_LIST = {
            "/login",
            "/users/registration",
            "/users/remember-password"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf().disable()
                .cors().disable()
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(RESOURCES_WHITE_LIST).permitAll()
                        .requestMatchers(FILMS_WHITE_LIST).permitAll()
                        .requestMatchers(DIRECTORS_WHITE_LIST).permitAll()
                        .requestMatchers(USERS_WHITE_LIST).permitAll()
                        .requestMatchers(FILMS_PERMISSIONS_LIST).hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(DIRECTORS_PERMISSIONS_LIST).hasAnyRole(ADMIN, MANAGER)
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/")
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .permitAll()
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                );
        return httpSecurity.build();
    }

    @Autowired
    protected void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }
}
