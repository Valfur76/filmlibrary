package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.FilmDTO;
import com.bagrov.springpmhw.videorent.model.Director;
import com.bagrov.springpmhw.videorent.model.Film;
import com.bagrov.springpmhw.videorent.service.DirectorService;
import com.bagrov.springpmhw.videorent.service.FilmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public String getAllFilms(Model model,
                              @ModelAttribute("director") Director director,
                              @ModelAttribute("film") Film film) {
        List<Film> films = filmService.findAllFilms();
        model.addAttribute("films", films);
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
//    public String addDirector(@RequestParam(value = "directorId") int directorId,
//                              @RequestParam(value = "filmId") int filmId,
//                              Model model) {
        public String addDirector(@RequestParam("filmTitle") String filmTitle,
                                  @RequestParam("directorsFIO") String directorsFIO) {
//        FilmDTO film = filmService.getOne(filmId);
//        DirectorDTO director = directorService.getOne(directorId);
//        film.getDirectors().add(director);
        filmService.addDirector(directorsFIO, filmTitle);
        return "redirect:/films";
    }


}
