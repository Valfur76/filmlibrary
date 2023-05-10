package com.bagrov.springpmhw.videorent.repository;

import com.bagrov.springpmhw.videorent.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Integer> {
}
