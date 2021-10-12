package com.works.controllers.adminpanel;

import com.works.entities.Customer;
import com.works.repositories._jpa.CustomerRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/customer")
public class CustomerController {

    final String rvalue = "adminpanel/customer/";
    final CustomerRepository customerRepository;

    public CustomerController(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @GetMapping("/list")
    public String customerList() {
        return rvalue + "customerlist";
    }

    @GetMapping("")
    public String customer(Model model) {
        model.addAttribute("customer", new Customer());
        model.addAttribute("isError", 0);
        return "adminpanel/customer/customeradd";
    }

    @PostMapping("/add")
    public String customerAdd(@Valid @ModelAttribute("customer") Customer customer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (Util.isEmail(customer.getCu_mail())) {
                if (Util.isTel(customer.getCu_tel())) {
                    try {
                        customerRepository.save(customer);
                    } catch (DataIntegrityViolationException e) {
                        System.err.println(e);
                        if (customerRepository.findByCu_mailEquals(customer.getCu_mail()).isPresent()) {
                            model.addAttribute("isError", 2);
                        } else {
                            model.addAttribute("isError", 4);
                        }
                        return rvalue + "customeradd";
                    }
                } else {
                    model.addAttribute("isError", 3);
                    return rvalue + "customeradd";
                }
            } else {
                model.addAttribute("isError", 1);
                return rvalue + "customeradd";
            }
            return "redirect:/admin/customer";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "customeradd";
        }
    }
}