package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.service.DirectorService;
import com.bagrov.springpmhw.videorent.service.FilmService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/films")
public class MVCFilmController {

    private final FilmService filmService;
    private final DirectorService directorService;

    public MVCFilmController(FilmService filmService, DirectorService directorService) {
        this.filmService = filmService;
        this.directorService = directorService;
    }

    @GetMapping
    public String getAllFilms(@RequestParam(value = "page", defaultValue = "1") int page,
                              @RequestParam(value = "size", defaultValue = "5") int pageSize,
                              Model model) {
        PageRequest pageRequest = PageRequest.of(page - 1, pageSize, Sort.by(Sort.Direction.ASC, "title"));
        Page<FilmDTO> result = filmService.findAllFilms(pageRequest);
        model.addAttribute("films", result);
        model.addAttribute("directors", directorService.findAll());
        return "films/allFilms";
    }

    @GetMapping("/add")
    public String create() {
        return "films/addFilm";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("filmForm") FilmDTO filmDTO) {
        filmService.save(filmDTO);
        return "redirect:/films";
    }

    @PostMapping(value = "/addDirector")
    public String addDirector(@RequestParam("filmTitle") String filmTitle,
                              @RequestParam("directorsFIO") String directorsFIO) {
        filmService.addDirector(directorsFIO, filmTitle);
        return "redirect:/films";
    }
}
