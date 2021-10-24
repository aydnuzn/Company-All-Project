package com.works.business._controllers.homepanel;

import com.works.entities.Contact;
import com.works.repositories._jpa.ContactRepository;
import com.works.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import java.util.Date;

@Service
public class HomeControllerBusiness {
    final String rvalue = "homepanel/";

    final ContactRepository contactRepository;

    public HomeControllerBusiness(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }


    public String home() {
        return rvalue + "home/home";
    }

    public String gallery() {
        return rvalue + "gallery/gallery";
    }

    public String contact(Model model) {
        model.addAttribute("contact", new Contact());
        model.addAttribute("isError", 0);
        return "homepanel/contact/contact";
    }

    public String contactAdd(Contact contact, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (Util.isEmail(contact.getContact_mail())) {
                //Validasyon ile de kontrol eklenebilir ileride.
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

    public String about() {
        return rvalue + "about/about";
    }

}
