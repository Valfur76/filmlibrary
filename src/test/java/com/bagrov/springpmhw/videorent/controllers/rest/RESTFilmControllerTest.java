package com.bagrov.springpmhw.videorent.controllers.rest;

import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.model.Genre;
import com.bagrov.springpmhw.videorent.service.FilmService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@Rollback
public class RESTFilmControllerTest extends CommonTestREST {

    @Autowired
    private FilmService filmService;

    private final FilmDTO filmDTO = new FilmDTO(100, "testTitle", "1900", "testCountry", Genre.FANTASY, new ArrayList<>(), new ArrayList<>());

    @Test
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void listAll() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/films")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(filmService.findAll())))
                .andReturn();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void getFilmById() throws Exception {
        int filmId = 5;
        mvc.perform(MockMvcRequestBuilders.get("/api/films/{id}", filmId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(filmService.getOne(filmId))))
                .andReturn();
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void addFilm() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/films/addFilm")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(filmDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("testTitle"))
                .andReturn();
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void updateFilm() throws Exception {
        int filmId = 5;
        mvc.perform(MockMvcRequestBuilders.put("/api/films/{id}", filmId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(filmDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("testTitle"))
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void deleteFilm() throws Exception {
        int filmId = 5;
        mvc.perform(MockMvcRequestBuilders.delete("/api/films/{id}", filmId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    @Order(5)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void orderFilm() throws Exception {
        int filmId = 1;
        int userId = 1;
        int rentPeriod = 7;
        mvc.perform(MockMvcRequestBuilders.post("/api/directors/{id}/orderFilm", filmId)
                        .param("userId", String.valueOf(userId))
                        .param("rentPeriod", String.valueOf(rentPeriod))
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andReturn();
    }
}
