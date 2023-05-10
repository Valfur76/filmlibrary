package com.bagrov.springpmhw.videorent.controller;

import com.bagrov.springpmhw.videorent.model.Director;
import com.bagrov.springpmhw.videorent.repository.DirectorRepository;
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
@RequestMapping("/directors")
@Tag(name = "Режиссеры", description = "Контроллер для работы с режиссерами")
public class DirectorController {

    private final DirectorRepository directorRepository;

    public DirectorController(DirectorRepository directorRepository) {
        this.directorRepository = directorRepository;
    }

    @Operation(description = "Получить список всех режиссеров")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Director>> getAllDirectors() {
        return ResponseEntity.status(HttpStatus.OK).body(directorRepository.findAll());
    }

    @Operation(description = "Получить режиссера по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Director> getDirectorById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(directorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Директор с указанным id не найден")));
    }

    @Operation(description = "Добавить режиссера")
    @PostMapping(value = "/addDirector",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Director> addDirector(@RequestBody Director director) {
        return ResponseEntity.status(HttpStatus.OK).body(directorRepository.save(director));
    }

    @Operation(description = "Изменить режиссера по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Director> updateDirector(@PathVariable("id") int id, @RequestBody Director director) {
        Optional<Director> optionalDirector = directorRepository.findById(id);
        if (optionalDirector.isPresent()) {
            Director updatedDirector = enrichDirector(optionalDirector, director);
            updatedDirector.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(directorRepository.save(updatedDirector));
        }
        throw new NotFoundException("Директор с указанным id не найден");
    }

    @Operation(description = "Удалить режиссера")
    @DeleteMapping("/{id}")
    public void deleteDirector(@PathVariable("id") int id) {
        if (directorRepository.findById(id).isPresent()) {
            directorRepository.deleteById(id);
        } else {
            throw new NotFoundException("Директор с указанным id не найден");
        }
    }

    protected Director enrichDirector(Optional<Director> optionalDirector, Director director) {
        Director updatedDirector = new Director();
        if (director.getDirectorsFIO() != null) {
            updatedDirector.setDirectorsFIO(director.getDirectorsFIO());
        } else {
            updatedDirector.setDirectorsFIO(optionalDirector.get().getDirectorsFIO());
        }

        if (director.getPosition() != null) {
            updatedDirector.setPosition(director.getPosition());
        } else {
            updatedDirector.setPosition(optionalDirector.get().getPosition());
        }

        if (director.getFilms() != null) {
            updatedDirector.setFilms(director.getFilms() );
        } else {
            updatedDirector.setFilms(optionalDirector.get().getFilms());
        }

        return updatedDirector;
    }
}
