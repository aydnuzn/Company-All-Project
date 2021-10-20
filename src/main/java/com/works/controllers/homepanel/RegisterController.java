package com.works.controllers.homepanel;

import com.works.entities.Company;
import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.security.Role;
import com.works.entities.security.User;
import com.works.properties.RegisterInterlayer;
import com.works.repositories._jpa.*;
import com.works.services.UserService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/register")
public class RegisterController {

    final String rvalue = "homepanel/register/";
    final CityRepository cityRepository;
    final DistrictRepository districtRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final RoleRepository roleRepository;
    final UserService userService;

    public RegisterController(CityRepository cityRepository, DistrictRepository districtRepository, CompanyRepository companyRepository, UserRepository userRepository, RoleRepository roleRepository, UserService userService) {
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userService = userService;
    }

    @GetMapping("")
    public String register(Model model) {
        model.addAttribute("cityList", cityRepository.findAll());
        model.addAttribute("districtList", districtRepository.findByCity_idEquals(1));
        model.addAttribute("registerInterlayer", new RegisterInterlayer());
        model.addAttribute("isError", 0);
        return "homepanel/register/register";
    }

    @PostMapping("/add")
    public String registerAdd(@Valid @ModelAttribute("registerInterlayer") RegisterInterlayer registerInterlayer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            //COMPANY
            Company company = new Company();
            company.setCompany_name(registerInterlayer.getCompany_name());
            company.setCompany_address(registerInterlayer.getCompany_address());
            company.setCompany_sector(registerInterlayer.getCompany_sector());
            company.setCompany_tel(registerInterlayer.getCompany_tel());
            Optional<City> optCity = cityRepository.findById(registerInterlayer.getCompany_city());
            if (optCity.isPresent()) {
                company.setCity(optCity.get());
            } else {
                //Veri tabanında City Yok.
                model.addAttribute("isError", 1);
                model.addAttribute("cityList", cityRepository.findAll());
                return rvalue + "register";
            }
            Optional<District> optDistrict = districtRepository.findById(registerInterlayer.getCompany_district());
            if (optDistrict.isPresent()) {
                company.setDistrict(optDistrict.get());
            } else {
                //Veri tabanında District Yok.
                model.addAttribute("isError", 2);
                model.addAttribute("cityList", cityRepository.findAll());
                return rvalue + "register";
            }
            //---ADMIN
            User admin = new User();
            admin.setName(registerInterlayer.getAdmin_name());
            admin.setSurname(registerInterlayer.getAdmin_surname());
            admin.setEmail(registerInterlayer.getAdmin_email());
            if (registerInterlayer.getAdmin_password1().equals(registerInterlayer.getAdmin_password2())) {
                admin.setPassword(userService.encoder().encode(registerInterlayer.getAdmin_password1()));
            } else {
                //Şifreler birbirinden farklı.
                model.addAttribute("isError", 3);
                model.addAttribute("cityList", cityRepository.findAll());
                return rvalue + "register";
            }
            admin.setTel(registerInterlayer.getAdmin_tel());
            admin.setCu_status(1);
            admin.setEnabled(true);
            admin.setTokenExpired(true);
            admin.setAddresses(null);
            Optional<Role> optRole = roleRepository.findById(1);
            List<Role> roleList = new ArrayList<>();
            if (optRole.isPresent()) {
                roleList.add(optRole.get());
                admin.setRoles(roleList);
            } else {
                //Veritabanında MVC Rolü Eksik.
                admin.setRoles(null);
            }
            Company company_ = new Company();
            try {
                company_ = companyRepository.save(company);
            } catch (DataIntegrityViolationException e) {
                if (companyRepository.findByCompany_nameEquals(company.getCompany_name()).isPresent()) {
                    //Firma adı aynısı mevcut.
                    model.addAttribute("isError", 4);
                    model.addAttribute("cityList", cityRepository.findAll());
                    return rvalue + "register";
                } else {
                    //Firma telefonu mevcut.
                    model.addAttribute("isError", 5);
                    model.addAttribute("cityList", cityRepository.findAll());
                    return rvalue + "register";
                }
            }
            try {
                admin.setCompany(company_);
                userRepository.save(admin);
            } catch (DataIntegrityViolationException e) {
                //Eklenen Şirketin silinmesi.
                companyRepository.delete(company_);
                if (userRepository.findByEmailEquals(admin.getEmail()).isPresent()) {
                    //Yönetici maili aynısı mevcut.
                    model.addAttribute("isError", 6);
                    model.addAttribute("cityList", cityRepository.findAll());
                    return rvalue + "register";
                } else {
                    //Yönetici telefon numarasının aynısı mevcut.
                    model.addAttribute("isError", 7);
                    model.addAttribute("cityList", cityRepository.findAll());
                    return rvalue + "register";
                }
            }
            model.addAttribute("isError", 0);
            return "redirect:/register";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            model.addAttribute("cityList", cityRepository.findAll());
            return rvalue + "register";
        }
    }

}
