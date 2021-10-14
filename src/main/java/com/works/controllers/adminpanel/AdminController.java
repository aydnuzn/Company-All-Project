package com.works.controllers.adminpanel;

import com.works.entities.security.User;
import com.works.repositories._jpa.UserRepository;
import com.works.utils.Util;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final String rvalue = "adminpanel/dashboard/";
    final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String dashboard() {
        return rvalue + "dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard2() {
        getCompany();
        return rvalue + "dashboard";
    }

    public void getCompany() {
        Optional<User> optUser = userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName());
        if (optUser.isPresent()) {
            Util.theCompany = optUser.get().getCompany();
        }
    }
}