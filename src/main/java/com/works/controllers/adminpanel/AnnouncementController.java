package com.works.controllers.adminpanel;

import com.works.entities.Announcement;
import com.works.entities.categories.AnnouncementCategory;
import com.works.models._elastic.AnnCategoryElastic;
import com.works.models._elastic.AnnouncementElastic;
import com.works.models._redis.AnnCategorySession;
import com.works.models._redis.AnnouncementSession;
import com.works.properties.AnnouncementInterlayer;
import com.works.repositories._elastic.AnnCategoryElasticRepository;
import com.works.repositories._elastic.AnnouncementElasticRepository;
import com.works.repositories._jpa.AnnCategoryRepository;
import com.works.repositories._jpa.AnnouncementRepository;
import com.works.repositories._redis.AnnCategorySessionRepository;
import com.works.repositories._redis.AnnouncementSessionRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/announcement")
public class AnnouncementController {

    final String rvalue = "adminpanel/announcement/";

    final AnnCategoryRepository annCategoryRepository;
    final AnnouncementRepository announcementRepository;

    final AnnCategorySessionRepository announcementCategorySessionRepository;
    final AnnouncementSessionRepository announcementSessionRepository;

    final AnnCategoryElasticRepository annCategoryElasticRepository;
    final AnnouncementElasticRepository announcementElasticRepository;

    public AnnouncementController(AnnCategoryRepository annCategoryRepository, AnnouncementRepository announcementRepository, AnnCategorySessionRepository announcementCategorySessionRepository, AnnouncementSessionRepository announcementSessionRepository, AnnCategoryElasticRepository annCategoryElasticRepository, AnnouncementElasticRepository announcementElasticRepository) {
        this.annCategoryRepository = annCategoryRepository;
        this.announcementRepository = announcementRepository;
        this.announcementCategorySessionRepository = announcementCategorySessionRepository;
        this.announcementSessionRepository = announcementSessionRepository;
        this.annCategoryElasticRepository = annCategoryElasticRepository;
        this.announcementElasticRepository = announcementElasticRepository;
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

    @GetMapping("/category/list")
    public String annCategoryList() {
        return rvalue + "anncategorylist";
    }

    @PostMapping("/category/add")
    public String annCategoryAdd(@Valid @ModelAttribute("announcementCategory") AnnouncementCategory announcementCategory, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()){
            try{
                announcementCategory.setAnnouncements(null);
                announcementCategory = annCategoryRepository.save(announcementCategory);
                /*----Add Redis Database---- */
                AnnCategorySession announcementCategorySession = new AnnCategorySession();
                announcementCategorySession.setId(announcementCategory.getId().toString());
                announcementCategorySession.setAnn_category_title(announcementCategory.getAnn_category_title());
                announcementCategorySessionRepository.save(announcementCategorySession);

                /*----Add Elasticsearch Database---- */
                AnnCategoryElastic annCategoryElastic = new AnnCategoryElastic();
                annCategoryElastic.setId(announcementCategory.getId().toString());
                annCategoryElastic.setAnn_category_title(announcementCategory.getAnn_category_title());
                annCategoryElasticRepository.save(annCategoryElastic);

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

    @GetMapping("/category/{stIndex}")
    public String announcementCategoryItem(@PathVariable String stIndex, Model model){
        try{
            Integer index = Integer.parseInt(stIndex);
            Optional<AnnouncementCategory> optionalAnnCategory = annCategoryRepository.findById(index);
            if(optionalAnnCategory.isPresent()){
                model.addAttribute("index", index );
                model.addAttribute("announcementCategory", optionalAnnCategory.get());
                model.addAttribute("isError", false);
                return "adminpanel/announcement/anncategoryupdate";
            }else{
                // ulasilmak istenen kategori mevcut değil
                return "error/404";
            }
        }catch(Exception ex){
            // url kısmında rakam girilecek yere string girilmis
            return "error/404";
        }
    }

    @PostMapping("/category/update/{stIndex}")
    public String annCategoryUpdate(@Valid @ModelAttribute("announcementCategory") AnnouncementCategory announcementCategory, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        // Kulanıcı formdaki id yi String yaparsa diye kontrol
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception ex) {
            return "error/404";
        }
        Optional<AnnouncementCategory> optionalAnnouncementCategory = annCategoryRepository.findById(index);
        // Kulanıcı formdaki id yi değiştirirse, o kategori var mı diye kontrol
        if(!bindingResult.hasErrors()){
            if(optionalAnnouncementCategory.isPresent()){
                try{
                    announcementCategory.setId(optionalAnnouncementCategory.get().getId());
                    announcementCategory = annCategoryRepository.saveAndFlush(announcementCategory);

                    /*----Update Redis Database---- */
                    AnnCategorySession announcementCategorySession = new AnnCategorySession();
                    announcementCategorySession.setId(announcementCategory.getId().toString());
                    announcementCategorySession.setAnn_category_title(announcementCategory.getAnn_category_title());
                    announcementCategorySessionRepository.deleteById(stIndex);
                    announcementCategorySessionRepository.save(announcementCategorySession);

                    /*----Update Elasticsearch Database---- */
                    AnnCategoryElastic annCategoryElastic = new AnnCategoryElastic();
                    annCategoryElastic.setId(announcementCategory.getId().toString());
                    annCategoryElastic.setAnn_category_title(announcementCategory.getAnn_category_title());
                    annCategoryElasticRepository.deleteById(stIndex);
                    annCategoryElasticRepository.save(annCategoryElastic);
                }catch(DataIntegrityViolationException ex){
                    System.err.println("Aynı isimde kategori mevcut");
                    model.addAttribute("index", index );
                    model.addAttribute("isError", true);
                    model.addAttribute("announcementCategory", optionalAnnouncementCategory.get());
                    return rvalue + "anncategoryupdate";
                }
                return "redirect:/admin/announcement/category/list";
            }else{
                return rvalue + "anncategoryupdate";
            }
        }else{
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("index", index );
        }
        model.addAttribute("isError", false);
        return rvalue + "anncategoryupdate";
    }

    /**********************************/

    @GetMapping("/list")
    public String announcementList() {
        return rvalue + "announcementlist";
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
                announcement = announcementRepository.save(announcement);
                /*----Add Redis Database---- */
                AnnouncementSession announcementSession = new AnnouncementSession();
                announcementSession.setId(announcement.getId().toString());
                announcementSession.setAnn_title(announcement.getAnn_title());
                announcementSession.setAnn_brief_description(announcement.getAnn_brief_description());
                String annType = announcement.getAnn_type() == 1 ? "Aktif":"Pasif";
                announcementSession.setAnn_type(annType);
                announcementSessionRepository.save(announcementSession);

                /*----Add Elasticsearch Database---- */
                AnnouncementElastic announcementElastic = new AnnouncementElastic();
                announcementElastic.setId(announcement.getId().toString());
                announcementElastic.setAnn_title(announcement.getAnn_title());
                announcementElastic.setAnn_brief_description(announcement.getAnn_brief_description());
                announcementElastic.setAnn_type(announcement.getAnn_type() == 1 ? "Aktif":"Pasif");
                announcementElasticRepository.save(announcementElastic);
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

    @GetMapping("/{stIndex}")
    public String announcementItem(@PathVariable String stIndex, Model model){
        try{
            Integer index = Integer.parseInt(stIndex);
            Optional<Announcement> optionalAnnouncement = announcementRepository.findById(index);
            AnnouncementInterlayer announcementInterlayer = new AnnouncementInterlayer(
              optionalAnnouncement.get().getAnn_title(),
              optionalAnnouncement.get().getAnn_brief_description(),
              optionalAnnouncement.get().getAnn_description(),
              optionalAnnouncement.get().getAnn_type(),
              optionalAnnouncement.get().getAnnouncementCategory().getId()
            );

            if(optionalAnnouncement.isPresent()){
                model.addAttribute("index", index );
                model.addAttribute("announcementInterlayer", announcementInterlayer);
                model.addAttribute("ls", annCategoryRepository.findAll());
                return "adminpanel/announcement/announcementupdate";
            }else{
                // ulasilmak istenen duyuru mevcut değil
                return "error/404";
            }
        }catch(Exception ex){
            // url kısmında rakam girilecek yere string girilmis
            return "error/404";
        }
    }

    @PostMapping("/update/{stIndex}")
    public String announcementUpdate(@Valid @ModelAttribute("announcementInterlayer") AnnouncementInterlayer announcementInterlayer, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        // Kulanıcı formdaki id yi String yaparsa diye kontrol
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception ex) {
            return "error/404";
        }
        Optional<Announcement> optionalAnnouncement = announcementRepository.findById(index);
        if(!bindingResult.hasErrors()){
            // Kullanıcı formdaki id yi değiştirirse, o kategori var mı diye kontrol
            if(optionalAnnouncement.isPresent()){
                    Announcement announcement = new Announcement();
                    Optional<AnnouncementCategory> optAnnouncementCategory = annCategoryRepository.findById(announcementInterlayer.getAnn_category());
                    announcement.setAnnouncementCategory(optAnnouncementCategory.get());
                    announcement.setAnn_title(announcementInterlayer.getAnn_title());
                    announcement.setAnn_brief_description(announcementInterlayer.getAnn_brief_description());
                    announcement.setAnn_description(announcementInterlayer.getAnn_description());
                    announcement.setAnn_type(announcementInterlayer.getAnn_type());
                    announcement.setId(index);
                    announcement = announcementRepository.saveAndFlush(announcement);

                    /*----Add Redis Database---- */
                    AnnouncementSession announcementSession = new AnnouncementSession();
                    announcementSession.setId(announcement.getId().toString());
                    announcementSession.setAnn_title(announcement.getAnn_title());
                    announcementSession.setAnn_brief_description(announcement.getAnn_brief_description());
                    String annType = announcement.getAnn_type() == 1 ? "Aktif":"Pasif";
                    announcementSession.setAnn_type(annType);
                    announcementSessionRepository.deleteById(announcement.getId().toString());
                    announcementSessionRepository.save(announcementSession);

                    /*----Add Elasticsearch Database---- */
                    AnnouncementElastic announcementElastic = new AnnouncementElastic();
                    announcementElastic.setId(announcement.getId().toString());
                    announcementElastic.setAnn_title(announcement.getAnn_title());
                    announcementElastic.setAnn_brief_description(announcement.getAnn_brief_description());
                    announcementElastic.setAnn_type(announcement.getAnn_type() == 1 ? "Aktif":"Pasif");
                    announcementElasticRepository.deleteById(announcement.getId().toString());
                    announcementElasticRepository.save(announcementElastic);
                    return "redirect:/admin/announcement/list";
            }else{
                return rvalue + "announcementupdate";
            }
        }else{
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("index", index);
        }
        model.addAttribute("ls", annCategoryRepository.findAll());
        return rvalue + "announcementupdate";
    }





}