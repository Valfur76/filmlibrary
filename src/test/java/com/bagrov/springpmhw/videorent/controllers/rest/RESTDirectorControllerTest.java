package com.bagrov.springpmhw.videorent.controllers.rest;

import com.bagrov.springpmhw.videorent.dto.DirectorDTO;
import com.bagrov.springpmhw.videorent.service.DirectorService;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@Rollback
public class RESTDirectorControllerTest extends CommonTestREST {

    @Autowired
    private DirectorService directorService;

    private final DirectorDTO directorDTO = new DirectorDTO("Test director", null, null);

    @Test
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void listAll() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/api/directors")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(directorService.findAll())))
                .andReturn();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void getDirectorById() throws Exception {
        int directorId = 5;
        mvc.perform(MockMvcRequestBuilders.get("/api/directors/{id}", directorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(asJsonString(directorService.getOne(directorId))))
                .andReturn();
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void addDirector() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/directors/addDirector")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(asJsonString(directorDTO))
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.directorsFIO").value("Test director"))
                .andReturn();
    }

    @Test
    @Order(3)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void deleteDirector() throws Exception {
        int directorId = 5;
        mvc.perform(MockMvcRequestBuilders.delete("/api/directors/{id}", directorId)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andReturn();
    }

    @Test
    @Order(4)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void addFilm() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/api/directors/addFilm")
                        .param("filmId", "1")
                        .param("directorId", "1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.jsonPath("$.filmsIds").isArray())
                .andReturn();
    }
}
