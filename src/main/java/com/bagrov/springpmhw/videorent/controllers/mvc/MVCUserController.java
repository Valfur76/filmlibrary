package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.UserDTO;
import com.bagrov.springpmhw.videorent.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import static com.bagrov.springpmhw.videorent.constants.UserRolesConstants.ADMIN;

@Controller
@RequestMapping("/users")
public class MVCUserController {

    private final UserService userService;

    public MVCUserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("userForm", new UserDTO());
        return "users/registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") UserDTO userDTO,
                               BindingResult bindingResult) {
        if (userDTO.getLogin().equalsIgnoreCase(ADMIN) || userService.getUserByLogin(userDTO.getLogin()) != null) {
            bindingResult.rejectValue("login", "error.login", "Такой логин уже существует");
            return "users/registration";
        }
        if (userService.getUserByEmail(userDTO.getEmail()) != null) {
            bindingResult.rejectValue("email", "error.email", "Такой e-mail уже существует");
            return "users/registration";
        }
        userService.save(userDTO);
        return "redirect:/login";
    }

    @GetMapping
    public String getAllUsers(Model model,
                              @RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "id"));
        Page<UserDTO> users = userService.findAllUsers(pageRequest);
        model.addAttribute("users", users);
        return "users/allUsers";
    }

    @GetMapping("/profile/{id}")
    public String userInfo(@PathVariable int id,
                            Model model) {
        UserDTO userDTO = userService.getOne(id);
        model.addAttribute("user", userDTO);
        return "users/profile";
    }

    @GetMapping("/profile/{id}/edit")
    public String editUser(@PathVariable int id,
                           Model model) {
        UserDTO userDTO = userService.getOne(id);
        model.addAttribute("user", userDTO);
        return "users/edit";
    }

    @PostMapping("/profile/{id}/edit")
    public String editUser(@PathVariable int id,
                                 @ModelAttribute("editForm") UserDTO userDTO) {
        userService.update(id, userDTO);
        return "redirect:/users/profile/{id}";
    }
}
