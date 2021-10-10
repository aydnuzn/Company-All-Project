package com.works.controllers.homepanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/forgotpassword")
public class ForgotPasswordController {

    final String rvalue = "homepanel/forgotpassword/";
    
    @GetMapping("")
    public String forgotpassword(){
        return rvalue+"forgotpassword";
    }

}
