package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/deneme")
public class Deneme {


    @GetMapping("")
    public String deneme() {
        return "deneme";
    }


}
