package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.CustomerControllerBusiness;
import com.works.properties.CustomerInterlayer;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    final CustomerControllerBusiness business;

    public CustomerController(CustomerControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String customer(Model model) {
        return business.customer(model);
    }

    @GetMapping("/list")
    public String customerList(Model model) {
        return business.customerList(model);
    }

    @PostMapping("/add")
    public String customerAdd(@Valid @ModelAttribute("customerInterlayer") CustomerInterlayer customerInterlayer, BindingResult bindingResult, Model model) {
        return business.customerAdd(customerInterlayer, bindingResult, model);
    }

}