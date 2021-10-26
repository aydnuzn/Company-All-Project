package com.works.business._restcontrollers.adminpanel;

import com.works.entities.projections.DashboardInfo;
import com.works.repositories._jpa.AnnouncementRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class AdminRestControllerBusiness {
    final AnnouncementRepository announcementRepository;

    public AdminRestControllerBusiness(AnnouncementRepository announcementRepository) {
        this.announcementRepository = announcementRepository;
    }

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
