package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.AdminControllerBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final AdminControllerBusiness business;

    public AdminController(AdminControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String dashboard(Model model) {
        return business.dashboard(model);
    }

    @GetMapping("/dashboard")
    public String dashboard2(Model model) {
        return business.dashboard(model);
    }
}