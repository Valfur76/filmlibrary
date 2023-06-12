//package com.bagrov.springpmhw.videorent.config.jwt;
//
//import com.bagrov.springpmhw.videorent.service.userdetails.CustomUserDetailsService;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
//import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.config.http.SessionCreationPolicy;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import java.util.List;
//
//import static com.bagrov.springpmhw.videorent.constants.UserRolesConstants.*;
//
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//public class JWTSecurityConfig {
//    private final CustomUserDetailsService customUserDetailsService;
//    private final JWTTokenFilter jwtTokenFilter;
//
//    public JWTSecurityConfig(CustomUserDetailsService customUserDetailsService,
//                             JWTTokenFilter jwtTokenFilter) {
//        this.customUserDetailsService = customUserDetailsService;
//        this.jwtTokenFilter = jwtTokenFilter;
//    }
//
//    private final String[] RESOURCES_WHITE_LIST = {
//            "/resources/**",
//            "/static/**",
//            "/js/**",
//            "/css/**",
//            "/",
//            "/swagger-ui/**",
//            "/webjars/bootstrap/5.3.0/**",
//            "/webjars/bootstrap/5.3.0/css/**",
//            "/webjars/bootstrap/5.3.0/js/**",
//            "/v3/api-docs/**"
//    };
//
//    private final String[] FILMS_PERMISSIONS_LIST = {
//            "/api/films/addFilm"
//    };
//
//    private final String[] DIRECTORS_PERMISSIONS_LIST = {
//            "/api/directors/addDirector"
//    };
//
//    private final String[] USERS_REST_WHITE_LIST = {"/api/users/auth"};
//
//
//    @Bean
//    public SecurityFilterChain restSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .cors().disable()
//                .csrf().disable()
//                .authorizeHttpRequests((requests) -> requests
//                        .requestMatchers(RESOURCES_WHITE_LIST).permitAll()
//                        .requestMatchers(USERS_REST_WHITE_LIST).permitAll()
//                        .requestMatchers(FILMS_PERMISSIONS_LIST).hasAnyRole(ADMIN, MANAGER)
//                        .requestMatchers(DIRECTORS_PERMISSIONS_LIST).hasAnyRole(ADMIN, MANAGER)
//                        .anyRequest().authenticated()
//                )
//                .exceptionHandling()
//                .and()
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class)
//                .userDetailsService(customUserDetailsService);
//
//        return httpSecurity.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
//        return authenticationConfiguration.getAuthenticationManager();
//    }
//}
