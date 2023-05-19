package com.hh.helping_hands_as.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/")
public class HomeController {

    @GetMapping
    public String demo() {
        return "home";
    }

    @ResponseBody
    @GetMapping("/secured")
    public String securedEndPoint() {
        return "Secured Demo";
    }
}
