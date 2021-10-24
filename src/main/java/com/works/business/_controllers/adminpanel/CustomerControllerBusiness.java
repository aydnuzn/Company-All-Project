package com.works.business._controllers.adminpanel;

import com.works.entities.security.Role;
import com.works.entities.security.User;
import com.works.models._elastic.CustomerElastic;
import com.works.models._redis.CustomerSession;
import com.works.properties.CustomerInterlayer;
import com.works.repositories._elastic.CustomerElasticRepository;
import com.works.repositories._jpa.RoleRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.CustomerSessionRepository;
import com.works.services.UserService;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerControllerBusiness {
    final String rvalue = "adminpanel/customer/";
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final CustomerElasticRepository customerElasticRepository;
    final CustomerSessionRepository customerSessionRepository;

    final UserService userService;

    public CustomerControllerBusiness(UserRepository userRepository, RoleRepository roleRepository, CustomerElasticRepository customerElasticRepository, CustomerSessionRepository customerSessionRepository, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.customerElasticRepository = customerElasticRepository;
        this.customerSessionRepository = customerSessionRepository;
        this.userService = userService;
    }

    public String customer(Model model) {
        model.addAttribute("customerInterlayer", new CustomerInterlayer());
        model.addAttribute("isError", 0);
        return rvalue + "customeradd";
    }

    public String customerList(Model model) {
        model.addAttribute("customerInterlayer", new CustomerInterlayer());
        model.addAttribute("isError", 0);
        return rvalue + "customerlist";
    }

    public String customerAdd(CustomerInterlayer customerInterlayer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            User customer = new User();
            customer.setName(customerInterlayer.getCu_name());
            customer.setSurname(customerInterlayer.getCu_surname());
            customer.setEmail(customerInterlayer.getCu_email());
            customer.setPassword(userService.encoder().encode(customerInterlayer.getCu_password()));

            customer.setTel(customerInterlayer.getCu_tel());
            customer.setCu_status(customerInterlayer.getCu_status());
            customer.setEnabled(true);
            customer.setTokenExpired(true);
            customer.setAddresses(null);
            Optional<Role> optRole = roleRepository.findById(3);
            List<Role> roleList = new ArrayList<>();
            if (optRole.isPresent()) {
                roleList.add(optRole.get());
                customer.setRoles(roleList);
            } else {
                //Veritabanında Müşteri Rolü Eksik.
                customer.setRoles(null);
            }
            try {
                customer = userRepository.save(customer);
            } catch (DataIntegrityViolationException e) {
                if (userRepository.findByEmailEquals(customer.getEmail()).isPresent()) {
                    model.addAttribute("isError", 1);
                    return rvalue + "customeradd";
                } else {
                    model.addAttribute("isError", 2);
                    return rvalue + "customeradd";
                }
            }

            CustomerSession customerSession = new CustomerSession();
            customerSession.setId(String.valueOf(customer.getId()));
            customerSession.setName(customer.getName());
            customerSession.setSurname(customer.getSurname());
            customerSession.setEmail(customer.getEmail());
            customerSession.setTel(customer.getTel());
            customerSession.setCu_status(customer.getCu_status());
            customerSessionRepository.save(customerSession);

            CustomerElastic customerElastic = new CustomerElastic();
            customerElastic.setId(String.valueOf(customer.getId()));
            customerElastic.setName(customer.getName());
            customerElastic.setSurname(customer.getSurname());
            customerElastic.setEmail(customer.getEmail());
            customerElastic.setTel(customer.getTel());
            customerElastic.setCu_status(customer.getCu_status());
            customerElasticRepository.save(customerElastic);

            return "redirect:/admin/customer";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "customeradd";
        }
    }

}
