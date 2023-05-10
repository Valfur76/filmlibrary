package com.bagrov.springpmhw.videorent.controller;

import com.bagrov.springpmhw.videorent.model.Film;
import com.bagrov.springpmhw.videorent.repository.FilmRepository;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.webjars.NotFoundException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/films")
@Tag(name = "Фильмы", description = "Контроллер для работы с фильмами")
public class FilmController {

    private final FilmRepository filmRepository;

    public FilmController(FilmRepository filmRepository) {
        this.filmRepository = filmRepository;
    }

    @Operation(description = "Получить список всех фильмов")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Film>> getAllFilms() {
        return ResponseEntity.status(HttpStatus.OK).body(filmRepository.findAll());
    }

    @Operation(description = "Получить фильм по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Film> getFilmById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(filmRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Фильм с указанным id не найден")));
    }

    @Operation(description = "Добавить фильм")
    @PostMapping(value = "/addFilm",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Film> addFilm(@RequestBody Film film) {
        return ResponseEntity.status(HttpStatus.OK).body(filmRepository.save(film));
    }

    @Operation(description = "Изменить фильм по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Film> updateFilm(@PathVariable("id") int id, @RequestBody Film film) {
        Optional<Film> optionalFilm = filmRepository.findById(id);
        if (optionalFilm.isPresent()) {
            Film updatedFilm = enrichFilm(optionalFilm, film);
            updatedFilm.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(filmRepository.save(updatedFilm));
        }
        throw new NotFoundException("Фильм с указанным id не найден");
    }

    @Operation(description = "Удалить фильм")
    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") int id) {
        if (filmRepository.findById(id).isPresent()) {
            filmRepository.deleteById(id);
        } else {
            throw new NotFoundException("Фильм с указанным id не найден");
        }
    }

    protected Film enrichFilm(Optional<Film> optionalFilm, Film film) {
        Film updatedFilm = new Film();
        if (film.getTitle() != null) {
            updatedFilm.setTitle(film.getTitle());
        } else {
            updatedFilm.setTitle(optionalFilm.get().getTitle());
        }

        if (film.getPremierYear() != null) {
            updatedFilm.setPremierYear(film.getPremierYear());
        } else {
            updatedFilm.setPremierYear(optionalFilm.get().getPremierYear());
        }

        if (film.getCountry() != null) {
            updatedFilm.setCountry(film.getCountry());
        } else {
            updatedFilm.setCountry(optionalFilm.get().getCountry());
        }

        if (film.getGenre() != null) {
            updatedFilm.setGenre(film.getGenre());
        } else {
            updatedFilm.setGenre(optionalFilm.get().getGenre());
        }

        if (film.getDirectors() != null) {
            updatedFilm.setDirectors(film.getDirectors());
        } else {
            updatedFilm.setDirectors(optionalFilm.get().getDirectors());
        }

        return updatedFilm;
    }
}
