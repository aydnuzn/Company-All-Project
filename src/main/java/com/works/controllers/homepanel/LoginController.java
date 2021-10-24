package com.works.controllers.homepanel;

import com.works.business._controllers.homepanel.LoginControllerBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/login")
public class LoginController {

    final LoginControllerBusiness business;

    public LoginController(LoginControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String login() {
        return business.login();
    }

}
