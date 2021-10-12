package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/content")
public class ContentController {

    final String rvalue = "adminpanel/content/";

    @GetMapping("/list")
    public String contentList() {
        return rvalue + "contentlist";
    }

    @GetMapping("/add")
    public String contentAdd() {
        return rvalue + "contentadd";
    }
}
