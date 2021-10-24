package com.works.business._controllers.adminpanel;

import com.works.entities.Company;
import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.security.Role;
import com.works.entities.security.User;
import com.works.properties.LocationChangeInterlayer;
import com.works.properties.LogoChangeInterlayer;
import com.works.properties.RegisterChangeInterlayer;
import com.works.properties.RegisterInterlayer;
import com.works.repositories._jpa.*;
import com.works.services.UserService;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SettingsControllerBusiness {
    final String rvalue = "adminpanel/settings/";
    final CityRepository cityRepository;
    final DistrictRepository districtRepository;
    final RoleRepository roleRepository;
    final CompanyRepository companyRepository;
    final UserRepository userRepository;
    final UserService userService;

    public SettingsControllerBusiness(CityRepository cityRepository, DistrictRepository districtRepository, RoleRepository roleRepository, CompanyRepository companyRepository, UserRepository userRepository, UserService userService) {
        this.cityRepository = cityRepository;
        this.districtRepository = districtRepository;
        this.roleRepository = roleRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    public String settings(Model model) {
        model.addAttribute("companyInfo", Util.theCompany);
        model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
        model.addAttribute("districtList", districtRepository.findByCity_idEquals(Util.theCompany.getCity().getId()));
        model.addAttribute("cityList", cityRepository.findAll());
        model.addAttribute("registerInterlayer", new RegisterInterlayer());
        model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
        model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
        model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
        model.addAttribute("isError", 0);
        return rvalue + "settings";
    }

    public String registerUpdate(RegisterInterlayer registerInterlayer, BindingResult bindingResult, Model model) {
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
                model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
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
                model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
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
                model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
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
                    model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                    model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                    return rvalue + "settings";
                } else {
                    //Firma telefonu mevcut.
                    model.addAttribute("isError", 5);
                    model.addAttribute("cityList", cityRepository.findAll());
                    model.addAttribute("companyInfo", Util.theCompany);
                    model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                    model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                    model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                    model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
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
                    model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                    model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                    return rvalue + "settings";
                } else {
                    //Yönetici telefon numarasının aynısı mevcut.
                    model.addAttribute("isError", 7);
                    model.addAttribute("cityList", cityRepository.findAll());
                    model.addAttribute("companyInfo", Util.theCompany);
                    model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                    model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                    model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                    model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                    return rvalue + "settings";
                }
            }
            return "redirect:/admin/settings";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("companyInfo", Util.theCompany);
            model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
            model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
            model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
            return rvalue + "settings";
        }
    }

    public String roleChange(RegisterChangeInterlayer registerChangeInterlayer, BindingResult bindingResult, Model model) {
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
                        model.addAttribute("isError", 0);
                        model.addAttribute("cityList", cityRepository.findAll());
                        model.addAttribute("companyInfo", Util.theCompany);
                        model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                        model.addAttribute("registerInterlayer", new RegisterInterlayer());
                        model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                        model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                        return rvalue + "settings";
                    }
                    optUser.get().setRoles(roleList);
                    userRepository.saveAndFlush(optUser.get());
                }
                model.addAttribute("isError", 0);
                return "redirect:/admin/settings";
            } catch (Exception e) {
                model.addAttribute("isError", 0);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerInterlayer", new RegisterInterlayer());
                model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
                model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                return rvalue + "settings";
            }
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("companyInfo", Util.theCompany);
            model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            model.addAttribute("registerInterlayer", new RegisterInterlayer());
            model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
            model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
            return rvalue + "settings";
        }
    }

    public String logoChange(LogoChangeInterlayer logoChangeInterlayer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            String fileName = StringUtils.cleanPath(logoChangeInterlayer.getChange_logo_file().getOriginalFilename());
            String ext = "";
            try {//File kısmı validation'da eksik kontrol edildiği için resim yüklenmemesi durumu kontrolü
                int length = fileName.lastIndexOf(".");
                ext = fileName.substring(length, fileName.length());
            } catch (Exception e) {
                model.addAttribute("isError", 8);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerInterlayer", new RegisterInterlayer());
                model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                return rvalue + "settings";
            }
            String uui = UUID.randomUUID().toString();
            fileName = uui + ext;
            try {

                // Logo resimlerinin tutulacagi Klasör olusturulacak
                File theDir = new File(Util.UPLOAD_DIR + "logos/" + Util.theCompany.getId());
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
                Path path = Paths.get(Util.UPLOAD_DIR + "logos/" + Util.theCompany.getId() + "/" + fileName);
                Files.copy(logoChangeInterlayer.getChange_logo_file().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                model.addAttribute("isError", 9);
                model.addAttribute("cityList", cityRepository.findAll());
                model.addAttribute("companyInfo", Util.theCompany);
                model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
                model.addAttribute("registerInterlayer", new RegisterInterlayer());
                model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
                model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
                return rvalue + "settings";
            }
            //add database
            Util.theCompany.setCompany_logo(fileName);
            companyRepository.saveAndFlush(Util.theCompany);
            return "redirect:/admin/settings";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("companyInfo", Util.theCompany);
            model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            model.addAttribute("registerInterlayer", new RegisterInterlayer());
            model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
            model.addAttribute("locationChangeInterlayer", new LocationChangeInterlayer());
            return rvalue + "settings";
        }
    }

    public String locationChange(LocationChangeInterlayer locationChangeInterlayer, BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            Util.theCompany.setCompany_lat(locationChangeInterlayer.getCompany_lat());
            Util.theCompany.setCompany_lng(locationChangeInterlayer.getCompany_lng());
            companyRepository.saveAndFlush(Util.theCompany);
            return "redirect:/admin/settings";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            model.addAttribute("cityList", cityRepository.findAll());
            model.addAttribute("companyInfo", Util.theCompany);
            model.addAttribute("adminInfo", userRepository.findByEmailEquals(SecurityContextHolder.getContext().getAuthentication().getName()).get());
            model.addAttribute("registerInterlayer", new RegisterInterlayer());
            model.addAttribute("registerChangeInterlayer", new RegisterChangeInterlayer());
            model.addAttribute("logoChangeInterlayer", new LogoChangeInterlayer());
            return rvalue + "settings";
        }
    }

}
