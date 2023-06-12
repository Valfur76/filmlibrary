package com.bagrov.springpmhw.videorent.controllers.mvc;

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
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@Rollback
public class MVCUserControllerTest extends CommonTestMVC {

    private final UserDTO userDTO = new UserDTO(7, "test", "test", "test", "test", "test", null, "test", "test", "test", null);


    @Test
    @Order(0)
    @WithMockUser(username = "admin", roles = "ADMIN", password = "admin")
    public void listAll() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("page", "1")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/allUsers"))
                .andExpect(model().attributeExists("users"))
                .andReturn();
    }

    @Test
    @Order(1)
    @WithAnonymousUser
    public void registrationGet() throws Exception {

        mvc.perform(MockMvcRequestBuilders.get("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                )
                .andExpect(status().is2xxSuccessful())
                .andExpect(view().name("users/registration"))
                .andExpect(model().attributeExists("userForm"))
                .andReturn();
    }

    @Test
    @Order(2)
    @WithAnonymousUser
    protected void registrationPost() throws Exception {
        mvc.perform(MockMvcRequestBuilders.post("/users/registration")
                        .contentType(MediaType.APPLICATION_JSON)
                        .flashAttr("userForm", userDTO)
                        .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/login"))
                .andExpect(redirectedUrlTemplate("/login"))
                .andExpect(redirectedUrl("/login"));
    }
}
