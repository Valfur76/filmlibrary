package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.OrderDTO;
import com.bagrov.springpmhw.videorent.service.FilmService;
import com.bagrov.springpmhw.videorent.service.OrderService;
import com.bagrov.springpmhw.videorent.service.userdetails.CustomUserDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/order")
public class MVCOrderController {

    private final FilmService filmService;
    private final OrderService orderService;

    public MVCOrderController(FilmService filmService, OrderService orderService) {
        this.filmService = filmService;
        this.orderService = orderService;
    }

    @GetMapping("/film/{filmId}")
    public String orderFilm(@PathVariable int filmId,
                            Model model) {
        model.addAttribute("film", filmService.getOne(filmId));
        return "/users/orderFilm";
    }

    @PostMapping("/film")
    public String orderFilm(@ModelAttribute("orderDTO") OrderDTO orderDTO) {
        CustomUserDetails customUserDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        orderDTO.setUserId(customUserDetails.getId());
        orderService.orderFilm(orderDTO);
        return "redirect:/order/user-films/" + customUserDetails.getId();
    }

    @GetMapping("/user-films/{id}")
    public String userFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                            @RequestParam(value = "size", defaultValue = "5") int pageSize,
                            @PathVariable int id,
                            Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize);
        Page<OrderDTO> rentInfoDTOPage = orderService.listUserOrderFilms(id, pageRequest);
        model.addAttribute("orderFilms", rentInfoDTOPage);
        model.addAttribute("userId", id);
        return "users/viewAllUserFilms";
    }
}
