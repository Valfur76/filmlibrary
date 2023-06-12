package com.bagrov.springpmhw.videorent.service.userdetails;

import com.bagrov.springpmhw.videorent.model.User;
import com.bagrov.springpmhw.videorent.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.bagrov.springpmhw.videorent.constants.UserRolesConstants.*;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Value("${spring.security.user.name}")
    private String adminUserName;
    @Value("${spring.security.user.password}")
    private String adminUserPassword;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        if (username.equals(adminUserName)) {
            return new CustomUserDetails(null,
                    username,
                    adminUserPassword,
                    List.of(new SimpleGrantedAuthority("ROLE_" + ADMIN)));
        } else {
            User user = userRepository.findUserByLogin(username);
            if (user != null) {
                List<GrantedAuthority> authorities = new ArrayList<>();
                authorities.add(new SimpleGrantedAuthority(
                        user.getRole().getId() == 1
                                ? "ROLE_" + USER
                                : "ROLE_" + MANAGER));
                return new CustomUserDetails(user.getId(), username, user.getPassword(), authorities);
            } else {
                throw new UsernameNotFoundException("Username not found");
            }
        }
    }
}
