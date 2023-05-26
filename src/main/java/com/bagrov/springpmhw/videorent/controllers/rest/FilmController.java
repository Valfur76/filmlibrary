package com.bagrov.springpmhw.videorent.controllers.rest;

import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.service.FilmService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/films")
@Tag(name = "Фильмы", description = "Контроллер для работы с фильмами")
public class FilmController {

    private final FilmService filmService;

    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @Operation(description = "Получить список всех фильмов")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilmDTO>> getAllFilms() {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.findAll());
    }

    @Operation(description = "Получить фильм по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmDTO> getFilmById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.getOne(id));
    }

    @Operation(description = "Добавить фильм")
    @PostMapping(value = "/addFilm",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmDTO> addFilm(@RequestBody FilmDTO filmDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.save(filmDTO));
    }

    @Operation(description = "Изменить фильм по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmDTO> updateFilm(@PathVariable("id") int id, @RequestBody FilmDTO filmDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.update(id, filmDTO));
    }

    @Operation(description = "Удалить фильм")
    @DeleteMapping("/{id}")
    public void deleteFilm(@PathVariable("id") int id) {
        filmService.delete(id);
    }

    @Operation(description = "Добавить режиссера к фильму")
    @PostMapping(value = "/addDirector",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FilmDTO> addDirector(@RequestParam(value = "directorId") int directorId,
                                               @RequestParam(value = "filmId") int filmId) {
        return ResponseEntity.status(HttpStatus.OK).body(filmService.addDirector(directorId, filmId));
    }

    @Operation(description = "Арендовать фильм")
    @PostMapping(value = "/{id}/rentFilm")
    public void rentFilm(@PathVariable("id") int id,
                         @RequestParam(value = "userId") int userId,
                         @RequestParam(value = "rentPeriod") int rentPeriod) {
        filmService.rentFilm(id, userId, rentPeriod);
    }
}
