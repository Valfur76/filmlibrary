package com.bagrov.springpmhw.videorent.config;

import com.bagrov.springpmhw.videorent.config.jwt.JWTTokenFilter;
import com.bagrov.springpmhw.videorent.service.userdetails.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.bagrov.springpmhw.videorent.constants.UserRolesConstants.*;
import static com.bagrov.springpmhw.videorent.constants.SecurityConstants.*;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class CommonSecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;
    private final JWTTokenFilter jwtTokenFilter;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public CommonSecurityConfig(CustomUserDetailsService customUserDetailsService,
                                JWTTokenFilter jwtTokenFilter,
                                BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenFilter = jwtTokenFilter;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain restSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .cors().disable()
                .csrf().disable()
                .securityMatcher("/api/**")
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers(RESOURCES_WHITE_LIST).permitAll()
                        .requestMatchers(USERS_REST_WHITE_LIST).permitAll()
                        .requestMatchers(FILMS_REST_PERMISSIONS_LIST).hasAnyRole(ADMIN, MANAGER)
                        .requestMatchers(DIRECTORS_REST_PERMISSIONS_LIST).hasAnyRole(ADMIN, MANAGER)
                        .anyRequest().authenticated()
                )
                .exceptionHandling()
                .and()
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .userDetailsService(customUserDetailsService);

        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

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
                        .requestMatchers(ADMIN_PERMISSION_LIST).hasRole(ADMIN)
                        .requestMatchers(USERS_PERMISSION_LIST).hasRole(USER)
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
