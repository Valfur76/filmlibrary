package com.bagrov.springpmhw.videorent.controllers.rest;

import com.bagrov.springpmhw.videorent.config.jwt.JWTTokenUtil;
import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.dto.LoginDTO;
import com.bagrov.springpmhw.videorent.dto.UserDTO;
import com.bagrov.springpmhw.videorent.service.FilmService;
import com.bagrov.springpmhw.videorent.service.UserService;
import com.bagrov.springpmhw.videorent.service.userdetails.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
public class UserController {

    private final UserService userService;
    private final FilmService filmService;
    private final CustomUserDetailsService customUserDetailsService;
    private final JWTTokenUtil jwtTokenUtil;

    public UserController(UserService userService, FilmService filmService, CustomUserDetailsService customUserDetailsService, JWTTokenUtil jwtTokenUtil) {
        this.userService = userService;
        this.filmService = filmService;
        this.customUserDetailsService = customUserDetailsService;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Operation(description = "Получить список всех пользователей")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDTO>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }

    @Operation(description = "Получить пользователя по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getOne(id));
    }

    @Operation(description = "Добавить пользователя")
    @PostMapping(value = "/addUser",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> addUser(@RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.save(userDTO));
    }

    @Operation(description = "Изменить пользователя по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDTO> updateUser(@PathVariable("id") int id, @RequestBody UserDTO userDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.update(id, userDTO));
    }

    @Operation(description = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        userService.delete(id);
    }

    @Operation(description = "Получить список всех фильмов у пользователя")
    @GetMapping(value = "/{id}/rentedFilms",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<FilmDTO>> rentedFilms(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.rentedFilms(id).stream()
                .map(filmService::convertToFilmDTO).toList());
    }

    @Operation(description = "Авторизация")
    @PostMapping("/auth")
    public ResponseEntity<?> auth(@RequestBody LoginDTO loginDTO) {
        Map<String, Object> response = new HashMap<>();
        UserDetails foundUser = customUserDetailsService.loadUserByUsername(loginDTO.getLogin());
        if (!userService.checkPassword(loginDTO.getPassword(), foundUser)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("ОШИБКА авторизации! Неверный пароль");
        }
        String token = jwtTokenUtil.generateToken(foundUser);
        response.put("token", token);
        response.put("username", foundUser.getUsername());
        response.put("role", foundUser.getAuthorities());
        return ResponseEntity.ok().body(response);
    }
}
