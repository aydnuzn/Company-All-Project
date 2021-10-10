package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/advertisement")
public class AdvertisementController {

    final String rvalue = "adminpanel/advertisement/";

    @GetMapping("/list")
    public String advertisementList() {
        return rvalue + "advertisementlist";
    }


    @GetMapping("/add")
    public String advertisementAdd() {
        return rvalue + "advertisementadd";
    }

}
