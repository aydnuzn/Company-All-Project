package com.works.controllers.adminpanel;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    final String rvalue = "adminpanel/customer/";

    @GetMapping("/list")
     public String customerList(){
        return rvalue + "customerlist";
    }

    @GetMapping("/add")
    public String customerAdd(){
        return rvalue + "customeradd";
    }

}
