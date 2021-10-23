package com.works.controllers.adminpanel;

import com.works.entities.projections.DashboardInfo;
import com.works.repositories._jpa.AnnouncementRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    final String rvalue = "adminpanel/dashboard/";
    final UserRepository userRepository;
    final AnnouncementRepository announcementRepository;

    public AdminController(UserRepository userRepository, AnnouncementRepository announcementRepository) {
        this.userRepository = userRepository;
        this.announcementRepository = announcementRepository;
    }


    @GetMapping("")
    public String dashboard(Model model) {
        Optional<DashboardInfo> optionalDashboardInfo = announcementRepository.getViewDashboard(Util.theCompany.getId());
        model.addAttribute("dashboardInfo", optionalDashboardInfo.get());
        return "adminpanel/dashboard/dashboard";
    }

    @GetMapping("/dashboard")
    public String dashboard2(Model model) {
        return "redirect:/admin";
    }
}