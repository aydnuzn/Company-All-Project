package com.works.controllers.adminpanel;

import com.works.entities.Company;
import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.security.Role;
import com.works.entities.security.User;
import com.works.properties.RegisterChangeInterlayer;
import com.works.properties.RegisterInterlayer;
import com.works.repositories._jpa.*;
import com.works.services.UserService;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
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
@RequestMapping("/admin/settings")
public class SettingsController {

    final String rvalue = "adminpanel/settings/";
    final CityRepository cityRepository;
    final DistrictRepository districtRepository;
    final RoleRepository roleRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final UserService userService;

    public SettingsController(CityRepository cityRepository, DistrictRepository districtRepository, RoleRepository roleRepository, CompanyRepository companyRepository, UserRepository userRepository, UserService userService) {
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }


    @GetMapping("")
    public String settings(Model model) {
        model.addAttribute("companyInfo", Util.theCompany);
        model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        model.addAttribute("districtList", districtRepository.findByCity_idEquals(Util.theCompany.getCity().getId()));
        model.addAttribute("cityList", cityRepository.findAll());
        model.addAttribute("registerInterlayer", new RegisterInterlayer());
        model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
        model.addAttribute("isError", 0);
        return "adminpanel/settings/settings";
    }

    @PostMapping("/update")
    public String registerUpdate(@Valid @ModelAttribute("registerInterlayer") RegisterInterlayer registerInterlayer, BindingResult bindingResult, Model model) {
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
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                return rvalue + "settings";
            }
            Optional<District> optDistrict = districtRepository.findById(registerInterlayer.getCompany_district());
            if (optDistrict.isPresent()) {
                company.setDistrict(optDistrict.get());
            } else {
                //Veri tabanında District Yok.
                model.addAttribute("isError", 2);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                return rvalue + "settings";
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
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                return rvalue + "settings";
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
                //GÜNCELLEME
                company.setId(Util.theCompany.getId());
                company_ = companyRepository.saveAndFlush(company);
            } catch (DataIntegrityViolationException e) {
                if (companyRepository.findByCompany_nameEquals(company.getCompany_name()).isPresent()) {
                    //Firma adı aynısı mevcut.
                    model.addAttribute("isError", 4);
                    model.addAttribute("cityList", cityRepository.findAll());
                    model.addAttribute("companyInfo", Util.theCompany);
                    model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                    model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                    return rvalue + "settings";
                } else {
                    //Firma telefonu mevcut.
                    model.addAttribute("isError", 5);
                    model.addAttribute("cityList", cityRepository.findAll());
                    model.addAttribute("companyInfo", Util.theCompany);
                    model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                    model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                    return rvalue + "settings";
                }
            }
            try {
                admin.setCompany(company_);
                //GÜNCELLEME
                admin.setId(userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get().getId());
                userRepository.saveAndFlush(admin);
            } catch (DataIntegrityViolationException e) {
                //Eklenen Şirketin silinmesi.
                companyRepository.delete(company_);
                if (userRepository.findByEmailEquals(admin.getEmail()).isPresent()) {
                    //Yönetici adı aynısı mevcut.
                    model.addAttribute("isError", 6);
                    model.addAttribute("cityList", cityRepository.findAll());
                    model.addAttribute("companyInfo", Util.theCompany);
                    model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                    model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                    return rvalue + "settings";
                } else {
                    //Yönetici telefon numarasının aynısı mevcut.
                    model.addAttribute("isError", 7);
                    model.addAttribute("cityList", cityRepository.findAll());
                    model.addAttribute("companyInfo", Util.theCompany);
                    model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                    model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                    return rvalue + "settings";
                }
            }
            model.addAttribute("isError", 0);
            return "redirect:/admin/settings";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("companyInfo", Util.theCompany);
            model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
            return rvalue + "settings";
        }
    }

    @PostMapping("/change")
    public String registerChange(@Valid @ModelAttribute("registerChangeInterlayer") RegisterChangeInterlayer registerChangeInterlayer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            Optional<User> optUser = userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName());
            try {
                List<Role> roleList = new ArrayList<>();
                for (int i = 0; i < registerChangeInterlayer.getChange_user_roles().length; i++) {
                    try {
                        Integer roleId = Integer.parseInt(registerChangeInterlayer.getChange_user_roles()[i]);
                        Optional<Role> optRole = roleRepository.findById(roleId);
                        if (optRole.isPresent()) {
                            roleList.add(optRole.get());
                        }
                    } catch (Exception e) {
                        model.addAttribute("isError", 3);
                        model.addAttribute("cityList", cityRepository.findAll());
                        model.addAttribute("companyInfo", Util.theCompany);
                        model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                        model.addAttribute("registerInterlayer", new RegisterInterlayer());
                        return rvalue + "settings";
                    }
                    optUser.get().setRoles(roleList);
                    userRepository.saveAndFlush(optUser.get());
                }
                return "redirect:/admin/settings";
            } catch (Exception e) {
                model.addAttribute("isError", 3);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerInterlayer", new RegisterInterlayer());
                return rvalue + "settings";
            }
        }
        model.addAttribute("isError", 3);
        model.addAttribute("cityList", cityRepository.findAll());
        model.addAttribute("companyInfo", Util.theCompany);
        model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        model.addAttribute("registerInterlayer", new RegisterInterlayer());
        return rvalue + "settings";
    }
}
