package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.AdminRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/admin")
public class AdminRestController {

    final AdminRestControllerBusiness business;

    public AdminRestController(AdminRestControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public Map<REnum, Object> dashboard(Model model) {
        return business.dashboard(model);
    }
}
