package com.bagrov.springpmhw.videorent.repository;

import com.bagrov.springpmhw.videorent.model.Film;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FilmRepository extends JpaRepository<Film, Integer> {
}
