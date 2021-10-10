package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/survey")
public class SurveyController {

    final String rvalue = "adminpanel/survey/";

    @GetMapping("/list")
    public String suveryList() {
        return rvalue + "surveylist";
    }

    @GetMapping("/add")
    public String suveryAdd() {
        return rvalue + "surveyadd";
    }

    @GetMapping("/detail")
    public String suveryDetail() {
        return rvalue + "surveydetail";
    }

    @GetMapping("/addOption")
    public String suveryOptionAdd() {
        return rvalue + "surveyoptionadd";
    }
}


