package com.works.controllers.homepanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/register")
public class RegisterController {

    final String rvalue = "homepanel/register/";

    @GetMapping("")
    public String register(){
        return rvalue+"register";
    }
}
