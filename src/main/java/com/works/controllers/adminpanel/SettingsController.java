package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/settings")
public class SettingsController {

    final String rvalue = "adminpanel/settings/";


    @GetMapping("")
    public String settings() {
        return rvalue + "settings";
    }
}
