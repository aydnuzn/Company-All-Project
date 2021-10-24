package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.SettingsControllerBusiness;
import com.works.properties.LocationChangeInterlayer;
import com.works.properties.LogoChangeInterlayer;
import com.works.properties.RegisterChangeInterlayer;
import com.works.properties.RegisterInterlayer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/settings")
public class SettingsController {

    final SettingsControllerBusiness business;

    public SettingsController(SettingsControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String settings(Model model) {
        return business.settings(model);
    }

    //Company - Müşteri güncelleme
    @PostMapping("/update")
    public String registerUpdate(@Valid @ModelAttribute("registerInterlayer") RegisterInterlayer registerInterlayer, BindingResult bindingResult, Model model) {
        return business.registerUpdate(registerInterlayer, bindingResult, model);
    }

    //Rol değiştirme (MVC - REST)
    @PostMapping("/change")
    public String roleChange(@Valid @ModelAttribute("registerChangeInterlayer") RegisterChangeInterlayer registerChangeInterlayer, BindingResult bindingResult, Model model) {
        return business.roleChange(registerChangeInterlayer, bindingResult, model);
    }

    @PostMapping("/changelogo")
    public String logoChange(@Valid @ModelAttribute("logoChangeInterlayer") LogoChangeInterlayer logoChangeInterlayer, BindingResult bindingResult, Model model) {
        return business.logoChange(logoChangeInterlayer, bindingResult, model);
    }

    @PostMapping("/changelocation")
    public String locationChange(@Valid @ModelAttribute("locationChangeInterlayer") LocationChangeInterlayer locationChangeInterlayer, BindingResult bindingResult, Model model) {
        return business.locationChange(locationChangeInterlayer, bindingResult, model);
    }

}