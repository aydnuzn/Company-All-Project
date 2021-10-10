package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/announcement")
public class AnnouncementController {

    final String rvalue = "adminpanel/announcement/";

    @GetMapping("/add")
    public String announcementAdd() {
        return rvalue + "announcementadd";
    }

    @GetMapping("/list")
    public String announcementList() {
        return rvalue + "announcementlist";
    }

    @GetMapping("/category/add")
    public String annCategoryAdd() {
        return rvalue + "anncategoryadd";
    }

    @GetMapping("/category/list")
    public String annCategoryList() {
        return rvalue + "anncategorylist";
    }

}