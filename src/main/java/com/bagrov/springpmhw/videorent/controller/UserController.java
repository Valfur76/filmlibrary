package com.bagrov.springpmhw.videorent.controller;

import com.bagrov.springpmhw.videorent.model.User;
import com.bagrov.springpmhw.videorent.repository.UserRepository;
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
@RequestMapping("/users")
@Tag(name = "Пользователи", description = "Контроллер для работы с пользователями")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Operation(description = "Получить список всех пользователей")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findAll());
    }

    @Operation(description = "Получить пользователя по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь с указанным id не найден")));
    }

    @Operation(description = "Добавить пользователя")
    @PostMapping(value = "/addUser",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> addUser(@RequestBody User user) {
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(user));
    }

    @Operation(description = "Изменить пользователя по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> updateUser(@PathVariable("id") int id, @RequestBody User user) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isPresent()) {
            User updatedUser = enrichUser(optionalUser, user);
            updatedUser.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(updatedUser));
        }
        throw new NotFoundException("Пользователь с указанным id не найден");
    }

    @Operation(description = "Удалить пользователя")
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable("id") int id) {
        if (userRepository.findById(id).isPresent()) {
            userRepository.deleteById(id);
        } else {
            throw new NotFoundException("Пользователь с указанным id не найден");
        }
    }

    protected User enrichUser(Optional<User> optionalUser, User user) {
        User updatedUser = new User();
        if (user.getLogin() != null) {
            updatedUser.setLogin(user.getLogin());
        } else {
            updatedUser.setLogin(optionalUser.get().getLogin());
        }

        if (user.getPassword() != null) {
            updatedUser.setPassword(user.getPassword());
        } else {
            updatedUser.setPassword(optionalUser.get().getPassword());
        }

        if (user.getFirstName() != null) {
            updatedUser.setFirstName(user.getFirstName());
        } else {
            updatedUser.setFirstName(optionalUser.get().getFirstName());
        }

        if (user.getLastName() != null) {
            updatedUser.setLastName(user.getLastName());
        } else {
            updatedUser.setLastName(optionalUser.get().getLastName());
        }

        if (user.getMiddleName() != null) {
            updatedUser.setMiddleName(user.getMiddleName());
        } else {
            updatedUser.setMiddleName(optionalUser.get().getMiddleName());
        }

        if (user.getBirthDate() != null) {
            updatedUser.setBirthDate(user.getBirthDate());
        } else {
            updatedUser.setBirthDate(optionalUser.get().getBirthDate());
        }

        if (user.getPhone() != null) {
            updatedUser.setPassword(user.getPhone());
        } else {
            updatedUser.setPassword(optionalUser.get().getPhone());
        }

        if (user.getAddress() != null) {
            updatedUser.setAddress(user.getAddress());
        } else {
            updatedUser.setAddress(optionalUser.get().getAddress());
        }

        if (user.getEmail() != null) {
            updatedUser.setEmail(user.getEmail());
        } else {
            updatedUser.setEmail(optionalUser.get().getEmail());
        }

        if (user.getCreatedWhen() != null) {
            updatedUser.setCreatedWhen(user.getCreatedWhen());
        } else {
            updatedUser.setCreatedWhen(optionalUser.get().getCreatedWhen());
        }

        if (user.getRole() != null) {
            updatedUser.setRole(user.getRole());
        } else {
            updatedUser.setRole(optionalUser.get().getRole());
        }

        return updatedUser;
    }
}
