package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.AdvertisementControllerBusiness;
import com.works.entities.Advertisement;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;


@Controller
@RequestMapping("/admin/advertisement")
public class AdvertisementController {

    final AdvertisementControllerBusiness business;

    public AdvertisementController(AdvertisementControllerBusiness business) {
        this.business = business;
    }


    @GetMapping("")
    public String advertisement(Model model) {
        return business.advertisement(model);
    }

    @GetMapping("/list")
    public String advertisementList() {
        return business.advertisementList();
    }


    @PostMapping("/update/{stIndex}")
    public String advertisementUpdate(@Valid @ModelAttribute("advertisement") Advertisement advertisement, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        return business.advertisementUpdate(advertisement, bindingResult, model, stIndex);
    }


    //update sayfasını açmak
    @GetMapping("/{stIndex}")
    public String advertisementUpdate(@PathVariable String stIndex, Model model) {
        return business.advertisementUpdate(stIndex, model);
    }


    @PostMapping("/add")
    public String advertisementAdd(@Valid @ModelAttribute("advertisement") Advertisement advertisement, BindingResult bindingResult, Model model, @RequestPart(value = "adv_image_file", required = false) MultipartFile adv_image_file) {
        return business.advertisementAdd(advertisement, bindingResult, model, adv_image_file);
    }
}
