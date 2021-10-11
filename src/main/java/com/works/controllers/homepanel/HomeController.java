package com.works.controllers.homepanel;

import com.works.entities.Contact;
import com.works.repositories._jpa.ContactRepository;
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

    final String rvalue = "homepanel/";

    final ContactRepository contactRepository;

    public HomeController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }

    @GetMapping("")
    public String home() {
        return rvalue + "home/home";
    }

    @GetMapping("/home")
    public String home2() {
        return rvalue + "home/home";
    }

    @GetMapping("/gallery")
    public String gallery() {
        return rvalue + "gallery/gallery";
    }

    @GetMapping("/contact")
    public String contact(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("isError", 0);
        return "homepanel/contact/contact";
    }

    @PostMapping("/contact/add")
    public String contactAdd(@Valid @ModelAttribute("contact") Contact contact, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (Util.isEmail(contact.getContact_mail())) {
                contact.setDate(new Date());
                contactRepository.save(contact);
            } else {
                model.addAttribute("isError", 1);
                System.out.println("Email format hatası");
                return rvalue + "contact/contact";
            }
            return "redirect:/contact";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "contact/contact";
        }
    }

    @GetMapping("/about")
    public String about() {
        return rvalue + "about/about";
    }


}
