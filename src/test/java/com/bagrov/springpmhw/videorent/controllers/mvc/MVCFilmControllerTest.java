package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.DirectorDTO;
import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.service.DirectorService;
import com.bagrov.springpmhw.videorent.service.FilmService;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.bagrov.springpmhw.videorent.model.Genre.FANTASY;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@Rollback
public class MVCFilmControllerTest extends CommonTestMVC {

    @Autowired
    private FilmService filmService;

    @Autowired
    private DirectorService directorService;

    private final FilmDTO filmDTO = new FilmDTO(1, "Film Title 1", "1900", "Country1", FANTASY, null, null);
    private final DirectorDTO directorDTO = new DirectorDTO("Test director", null, null);

    @Test
    @Order(0)
    @WithAnonymousUser
    @Override
    public void listAll() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/films")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("films/allFilms"))
                .andExpect(model().attributeExists("films"))
                .andExpect(model().attributeExists("directors"))
                .andReturn();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void create() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/films/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("filmForm", filmDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrlTemplate("/films"))
                .andExpect(redirectedUrl("/films"));
    }

    @Test
    @Order(2)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void addDirector() throws Exception {
        filmService.save(filmDTO);
        directorService.save(directorDTO);
        mvc.perform(MockMvcRequestBuilders.post("/films/addDirector")
                        .param("filmTitle", filmDTO.getTitle())
                        .param("directorsFIO", directorDTO.getDirectorsFIO())
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/films"))
                .andExpect(redirectedUrlTemplate("/films"))
                .andExpect(redirectedUrl("/films"));
    }
}
