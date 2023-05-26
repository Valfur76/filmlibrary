package com.bagrov.springpmhw.videorent.controllers.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MVCMainController {

    @GetMapping("/")
    public String index()    {
        return "index";
    }
}
