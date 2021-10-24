package com.works.business._controllers.adminpanel;

import com.works.entities.projections.DashboardInfo;
import com.works.repositories._jpa.AnnouncementRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

@Service
public class AdminControllerBusiness {
    final String rvalue = "adminpanel/dashboard/";
    final UserRepository userRepository;
    final AnnouncementRepository announcementRepository;

    public AdminControllerBusiness(UserRepository userRepository, AnnouncementRepository announcementRepository) {
        this.userRepository = userRepository;
        this.announcementRepository = announcementRepository;
    }

    public String dashboard(Model model) {
        Optional<DashboardInfo> optionalDashboardInfo = announcementRepository.getViewDashboard(Util.theCompany.getId());
        model.addAttribute("dashboardInfo", optionalDashboardInfo.get());
        return rvalue + "dashboard";
    }


}
