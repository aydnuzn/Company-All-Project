package com.works.business._controllers.homepanel;

import org.springframework.stereotype.Service;

@Service
public class LoginControllerBusiness {
    final String rvalue = "homepanel/login/";

    public String login() {
        return rvalue + "login";
    }
}
