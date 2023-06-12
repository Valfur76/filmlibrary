package com.bagrov.springpmhw.videorent.controllers.rest;

import com.bagrov.springpmhw.videorent.dto.LoginDTO;
import com.bagrov.springpmhw.videorent.dto.UserDTO;
import com.bagrov.springpmhw.videorent.service.UserService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Rollback
public class RESTUserControllerTest extends CommonTestREST {

    @Autowired
    private UserService userService;

    private final UserDTO userDTO = new UserDTO(7, "test", "test", "test", "test", "test", null, "test", "test", "test", null);
    private final LoginDTO loginDTO = new LoginDTO("user", "user");

    @Test
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void listAll() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(userService.findAll())))
                .andReturn();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void getUserById() throws Exception {
        int userId = 5;
        mvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(userService.getOne(userId))))
                .andReturn();
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void addUser() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/users/addUser")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(userDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("test"))
                .andReturn();
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void updateUser() throws Exception {
        int userId = 5;
        mvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(userDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("test"))
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void deleteUser() throws Exception {
        int userId = 5;
        mvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void rentedFilms() throws Exception {
        int userId = 1;
        mvc.perform(MockMvcRequestBuilders.get("/api/users/{id}/rentedFilms", userId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(userService.rentedFilms(userId))))
                .andReturn();
    }

    @Test
    @Order(6)
    @WithAnonymousUser
    public void auth() throws Exception {

        mvc.perform(MockMvcRequestBuilders.post("/api/users/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(loginDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("user"))
                .andReturn();
    }
}
