package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.SettingsRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/rest/admin/settings")
public class SettingsRestController {


    final SettingsRestControllerBusiness business;

    public SettingsRestController(SettingsRestControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("/getLocation")
    public Map<REnum, Object> getLocation() {
        return business.getLocation();
    }
}
