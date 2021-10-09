package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @GetMapping("/list")
    public String suveryList() {
        return "surveylist";
    }

    @GetMapping("/add")
    public String suveryAdd() {
        return "surveyadd";
    }

    @GetMapping("/detail")
    public String suveryDetail() {
        return "surveydetail";
    }

    @GetMapping("/addOption")
    public String suveryOptionAdd() {
        return "surveyoptionadd";
    }
}


