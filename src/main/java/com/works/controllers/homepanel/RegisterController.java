package com.works.controllers.homepanel;

import com.works.business._controllers.homepanel.RegisterControllerBusiness;
import com.works.properties.RegisterInterlayer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/register")
public class RegisterController {
    final RegisterControllerBusiness business;

    public RegisterController(RegisterControllerBusiness business) {
        this.business = business;
    }


    @GetMapping("")
    public String register(Model model) {
        return business.register(model);
    }

    @PostMapping("/add")
    public String registerAdd(@Valid @ModelAttribute("registerInterlayer") RegisterInterlayer registerInterlayer, BindingResult bindingResult, Model model) {
        return business.registerAdd(registerInterlayer, bindingResult, model);
    }

    @GetMapping("/mailverification/{stKey}")
    public String mailVerification(@PathVariable String stKey) {
        return business.mailVerification(stKey);
    }

}
