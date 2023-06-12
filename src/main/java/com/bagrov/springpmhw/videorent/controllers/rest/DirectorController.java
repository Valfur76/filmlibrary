package com.bagrov.springpmhw.videorent.controllers.rest;

import com.bagrov.springpmhw.videorent.dto.DirectorDTO;
import com.bagrov.springpmhw.videorent.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/directors")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Режиссеры", description = "Контроллер для работы с режиссерами")
public class DirectorController {

    private final DirectorService directorService;

    public DirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @Operation(description = "Получить список всех режиссеров")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<DirectorDTO>> getAllDirectors() {
        return ResponseEntity.status(HttpStatus.OK).body(directorService.findAll());
    }

    @Operation(description = "Получить режиссера по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDTO> getDirectorById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(directorService.getOne(id));
    }

    @Operation(description = "Добавить режиссера")
    @PostMapping(value = "/addDirector",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDTO> addDirector(@RequestBody DirectorDTO directorDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(directorService.save(directorDTO));
    }

    @Operation(description = "Изменить режиссера по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDTO> updateDirector(@PathVariable("id") int id, @RequestBody DirectorDTO directorDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(directorService.update(id, directorDTO));
    }

    @Operation(description = "Удалить режиссера")
    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable("id") int id) {
        directorService.delete(id);
    }

    @Operation(description = "Добавить фильм к режиссеру")
    @PostMapping(value = "/addFilm",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DirectorDTO> addFilm(@RequestParam(value = "filmId") int filmId,
                                             @RequestParam(value = "directorId") int directorId)   {
        return ResponseEntity.status(HttpStatus.OK).body(directorService.addFilm(filmId, directorId));
    }
}
