package com.bagrov.springpmhw.videorent.controllers.rest;


import com.bagrov.springpmhw.videorent.dto.RoleDTO;
import com.bagrov.springpmhw.videorent.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
@SecurityRequirement(name = "Bearer Authentication")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Роли", description = "Контроллер для работы с ролями")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @Operation(description = "Получить список всех ролей")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.findAll());
    }

    @Operation(description = "Получить роль по id")
    @GetMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable("id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.getOne(id));
    }

    @Operation(description = "Добавить роль")
    @PostMapping(value = "/addRole",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> addRole(@RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.save(roleDTO));
    }

    @Operation(description = "Изменить роль по id")
    @PutMapping(value = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<RoleDTO> updateRole(@PathVariable("id") int id, @RequestBody RoleDTO roleDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(roleService.update(id, roleDTO));
    }

    @Operation(description = "Удалить роль")
    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable("id") int id) {
        roleService.delete(id);
    }
}
