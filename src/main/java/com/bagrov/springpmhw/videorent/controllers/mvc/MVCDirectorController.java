package com.bagrov.springpmhw.videorent.controllers.mvc;

import com.bagrov.springpmhw.videorent.dto.DirectorDTO;
import com.bagrov.springpmhw.videorent.service.DirectorService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/directors")
public class MVCDirectorController {

    private final DirectorService directorService;

    public MVCDirectorController(DirectorService directorService) {
        this.directorService = directorService;
    }

    @GetMapping
    public String getAllDirectors(Model model) {
        List<DirectorDTO> directors = directorService.findAll();
        model.addAttribute("directors", directors);
        return "directors/allDirectors";
    }

    @GetMapping("/add")
    public String create()  {
        return "directors/addDirector";
    }

    @PostMapping("/add")
    public String create(@ModelAttribute("directorForm") DirectorDTO directorDTO)    {
        directorService.save(directorDTO);
        return "redirect:/directors";
    }
}
