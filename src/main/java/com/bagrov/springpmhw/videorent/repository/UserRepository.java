package com.bagrov.springpmhw.videorent.repository;

import com.bagrov.springpmhw.videorent.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserByLogin(String login);

    User findUserByEmail(String email);
}
