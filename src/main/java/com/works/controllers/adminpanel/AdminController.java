package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/dashboard")
public class AdminController {

    final String rvalue = "adminpanel/dashboard/";

    @GetMapping("")
    public String dashboard() {
        return rvalue + "dashboard";
    }

}
