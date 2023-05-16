package com.bagrov.springpmhw.videorent.repository;

import com.bagrov.springpmhw.videorent.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
}
