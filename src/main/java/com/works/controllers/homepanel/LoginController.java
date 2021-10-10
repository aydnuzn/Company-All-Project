package com.works.controllers.homepanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    final String rvalue = "homepanel/login/";

    @GetMapping("")
    public String login(){
        return rvalue+"login";
    }

}
