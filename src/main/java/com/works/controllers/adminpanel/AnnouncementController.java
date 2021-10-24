package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.AnnouncementControllerBusiness;
import com.works.entities.categories.AnnouncementCategory;
import com.works.properties.AnnouncementInterlayer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/announcement")
public class AnnouncementController {

    final AnnouncementControllerBusiness business;

    public AnnouncementController(AnnouncementControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String announcement(Model model) {
        return business.announcement(model);
    }

    @GetMapping("/category")
    public String announcementCategory(Model model) {
        return business.announcementCategory(model);
    }

    @GetMapping("/category/list")
    public String annCategoryList() {
        return business.annCategoryList();
    }

    @PostMapping("/category/add")
    public String annCategoryAdd(@Valid @ModelAttribute("announcementCategory") AnnouncementCategory announcementCategory, BindingResult bindingResult, Model model) {
        return business.annCategoryAdd(announcementCategory, bindingResult, model);
    }

    @GetMapping("/category/{stIndex}")
    public String announcementCategoryItem(@PathVariable String stIndex, Model model) {
        return business.announcementCategoryItem(stIndex, model);
    }

    @PostMapping("/category/update/{stIndex}")
    public String annCategoryUpdate(@Valid @ModelAttribute("announcementCategory") AnnouncementCategory announcementCategory, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        return business.annCategoryUpdate(announcementCategory, bindingResult, stIndex, model);
    }

    /**********************************/

    @GetMapping("/list")
    public String announcementList() {
        return business.announcementList();
    }

    @PostMapping("/add")
    public String announcementAdd(@Valid @ModelAttribute("announcementInterlayer") AnnouncementInterlayer announcementInterlayer, BindingResult bindingResult, Model model) {
        return business.announcementAdd(announcementInterlayer, bindingResult, model);
    }

    @GetMapping("/{stIndex}")
    public String announcementItem(@PathVariable String stIndex, Model model) {
        return business.announcementItem(stIndex, model);
    }

    @PostMapping("/update/{stIndex}")
    public String announcementUpdate(@Valid @ModelAttribute("announcementInterlayer") AnnouncementInterlayer announcementInterlayer, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        return business.announcementUpdate(announcementInterlayer, bindingResult, stIndex, model);
    }

}