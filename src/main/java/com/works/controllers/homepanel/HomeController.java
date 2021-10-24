package com.works.controllers.homepanel;

import com.works.business._controllers.homepanel.HomeControllerBusiness;
import com.works.entities.Contact;
import com.works.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Date;

@Controller
public class HomeController {

    final HomeControllerBusiness business;

    public HomeController(HomeControllerBusiness business) {
        this.business = business;
    }


    @GetMapping("")
    public String home() {
        return business.home();
    }

    @GetMapping("/home")
    public String home2() {
        return business.home();
    }

    @GetMapping("/gallery")
    public String gallery() {
        return business.gallery();
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        return business.contact(model);
    }

    @PostMapping("/contact/add")
    public String contactAdd(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult, Model model) {
        return business.contactAdd(contact, bindingResult, model);
    }

    @GetMapping("/about")
    public String about() {
        return business.about();
    }

}
