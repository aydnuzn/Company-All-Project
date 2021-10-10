package com.works.controllers.homepanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    final String rvalue = "homepanel/";

    @GetMapping("")
    public String home() {
        return rvalue+"home/home";
    }

    @GetMapping("/home")
    public String home2() {
        return rvalue+"home/home";
    }

    @GetMapping("/gallery")
    public String gallery() {
        return rvalue+"gallery/gallery";
    }

    @GetMapping("/contact")
    public String contact() {
        return rvalue+"contact/contact";
    }

    @GetMapping("/about")
    public String about() {
        return rvalue+"about/about";
    }



}
