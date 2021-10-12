package com.works.controllers.adminpanel;

import com.works.entities.Announcement;
import com.works.entities.categories.AnnouncementCategory;
import com.works.properties.AnnouncementInterlayer;
import com.works.repositories._jpa.AnnCategoryRepository;
import com.works.repositories._jpa.AnnouncementRepository;
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
import java.util.Optional;

@Controller
@RequestMapping("/admin/announcement")
public class AnnouncementController {

    final String rvalue = "adminpanel/announcement/";

    final AnnCategoryRepository annCategoryRepository;
    final AnnouncementRepository announcementRepository;

    public AnnouncementController(AnnCategoryRepository annCategoryRepository, AnnouncementRepository announcementRepository) {
        this.annCategoryRepository = annCategoryRepository;
        this.announcementRepository = announcementRepository;
    }

    @GetMapping("")
    public String announcement(Model model){
        model.addAttribute("announcementInterlayer", new AnnouncementInterlayer());
        model.addAttribute("ls", annCategoryRepository.findAll());
        model.addAttribute("isError", 1);
        return "adminpanel/announcement/announcementadd";
    }

    @GetMapping("/category")
    public String announcementCategory(Model model){
        model.addAttribute("announcementCategory", new AnnouncementCategory());
        model.addAttribute("isError", false);
        return "adminpanel/announcement/anncategoryadd";
    }

    @PostMapping("/add")
    public String announcementAdd(@Valid @ModelAttribute("announcementInterlayer") AnnouncementInterlayer announcementInterlayer, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()){
            Announcement announcement = new Announcement();
            Optional<AnnouncementCategory> optAnnouncementCategory = annCategoryRepository.findById(announcementInterlayer.getAnn_category());
            if(optAnnouncementCategory.isPresent()){
                announcement.setAnnouncementCategory(optAnnouncementCategory.get());
                announcement.setAnn_title(announcementInterlayer.getAnn_title());
                announcement.setAnn_brief_description(announcementInterlayer.getAnn_brief_description());
                announcement.setAnn_description(announcementInterlayer.getAnn_description());
                announcement.setAnn_type(announcementInterlayer.getAnn_type());
                announcementRepository.save(announcement);
                System.out.println("Added Announcement");
                return "redirect:/admin/announcement";
            }else{
                System.err.println("Lütfen mevcut bir kategori seçiniz");
                model.addAttribute("isError", 0);
                return rvalue + "announcementadd";
            }
        }else{
            System.err.println(Util.errors(bindingResult));
        }
        model.addAttribute("isError", 1);
        model.addAttribute("ls", annCategoryRepository.findAll());
        return rvalue + "announcementadd";
    }

    @GetMapping("/list")
    public String announcementList() {
        return rvalue + "announcementlist";
    }

    @PostMapping("/category/add")
    public String annCategoryAdd(@Valid @ModelAttribute("announcementCategory") AnnouncementCategory announcementCategory, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()){
            try{
                announcementCategory.setAnnouncements(null);
                annCategoryRepository.save(announcementCategory);
            }catch(DataIntegrityViolationException ex){
                System.err.println("Aynı isimde kategori mevcut");
                model.addAttribute("isError", true);
                return rvalue + "anncategoryadd";
            }
            return "redirect:/admin/announcement/category";
        }else{
            System.err.println(Util.errors(bindingResult));
        }
        model.addAttribute("isError", false);
        return rvalue + "anncategoryadd";
    }

    @GetMapping("/category/list")
    public String annCategoryList() {
        return rvalue + "anncategorylist";
    }

}