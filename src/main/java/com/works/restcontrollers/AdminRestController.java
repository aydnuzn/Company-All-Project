package com.works.restcontrollers;

import com.works.entities.projections.DashboardInfo;
import com.works.repositories._jpa.AnnouncementRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin")
public class AdminRestController {

    final AnnouncementRepository announcementRepository;

    public AdminRestController(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

    @GetMapping("")
    public Map<REnum, Object> dashboard(Model model) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        Optional<DashboardInfo> optionalDashboardInfo = announcementRepository.getViewDashboard(Util.theCompany.getId());
        model.addAttribute("dashboardInfo", optionalDashboardInfo.get());
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "İşlem Başarılı");
        hm.put(REnum.RESULT, optionalDashboardInfo.get());
        return hm;
    }
}
