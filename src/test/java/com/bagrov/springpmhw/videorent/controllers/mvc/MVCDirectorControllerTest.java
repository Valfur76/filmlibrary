package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.DirectorDTO;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@Transactional
@Rollback
public class MVCDirectorControllerTest extends CommonTestMVC {

    private final DirectorDTO directorDTO = new DirectorDTO("Test director", null, null);

    @Test
    @Order(0)
    @WithAnonymousUser
    public void listAll() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/directors")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("directors/allDirectors"))
                .andExpect(model().attributeExists("directors"))
                .andReturn();
    }

    @Test
    @Order(1)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    protected void create() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/directors/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("directorForm", directorDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/directors"))
                .andExpect(redirectedUrlTemplate("/directors"))
                .andExpect(redirectedUrl("/directors"));
    }
}
