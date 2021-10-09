package com.works.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/advertisement")
public class AdvertisementController {

    @GetMapping("/list")
    public String advertisementList(){
        return "advertisementlist";
    }


    @GetMapping("/add")
    public String advertisementAdd(){
        return "advertisementadd";
    }
}
