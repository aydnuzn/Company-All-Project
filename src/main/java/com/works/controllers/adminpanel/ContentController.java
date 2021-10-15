package com.works.controllers.adminpanel;

import com.works.entities.Content;
import com.works.models._elastic.ContentElasticsearch;
import com.works.models._redis.ContentSession;
import com.works.repositories._elastic.ContentElasticRepository;
import com.works.repositories._jpa.ContentRepository;
import com.works.repositories._redis.ContentSessionRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/admin/content")
public class ContentController {

    final String rvalue = "adminpanel/content/";

    final ContentRepository contentRepository;
    final ContentSessionRepository contentSessionRepository;
    final ContentElasticRepository contentElasticRepository;

    public ContentController(ContentRepository contentRepository, ContentSessionRepository contentSessionRepository, ContentElasticRepository contentElasticRepository) {
        this.contentRepository = contentRepository;
        this.contentSessionRepository = contentSessionRepository;
        this.contentElasticRepository = contentElasticRepository;
    }

    @GetMapping("")
    public String content(Model model) {
        model.addAttribute("content", new Content());
        model.addAttribute("isError", 0);
        return "adminpanel/content/contentadd";
    }

    @GetMapping("/list")
    public String contentList() {
        contentSessionRepository.findAll();
        return "adminpanel/content/contentlist";
    }

    @PostMapping("/add")
    public String contentAdd(@Valid @ModelAttribute("content") Content content, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()){
            try {
                //REDIS
                String contentId = String.valueOf(contentRepository.save(content).getId());
                ContentSession contentSession = new ContentSession();
                contentSession.setContent_title(content.getContent_title());
                contentSession.setContent_brief_description(content.getContent_brief_description());
                contentSession.setContent_detailed_description(content.getContent_detailed_description());
                contentSession.setContent_status(String.valueOf(content.getContent_status()));
                contentSession.setId(contentId);
                contentSessionRepository.save(contentSession);
                //ELASTICSEARCH
                ContentElasticsearch contentElasticsearch = new ContentElasticsearch();
                contentElasticsearch.setContent_title(content.getContent_title());
                contentElasticsearch.setContent_brief_description(content.getContent_brief_description());
                contentElasticsearch.setContent_detailed_description(content.getContent_detailed_description());
                contentElasticsearch.setContent_status(String.valueOf(content.getContent_status()));
                contentElasticsearch.setId(contentId);
                contentElasticRepository.save(contentElasticsearch);

            } catch (Exception e) {
                System.out.println("Error: " + e);
                model.addAttribute("isError", 1);
                return rvalue + "contentadd";
            }
        }else{
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "contentadd";
        }


        return "redirect:/admin/content";
    }

    @PostMapping("/update/{stIndex}")
    public String contentUpdate(@Valid @ModelAttribute("content") Content content, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception e) {
            return "error/404";
        }
        if (!bindingResult.hasErrors()) {
            try {
                content.setId(index);
                contentRepository.saveAndFlush(content);

                //REDIS
                String contentId = String.valueOf(contentRepository.save(content).getId());
                ContentSession contentSession = new ContentSession();
                contentSession.setContent_title(content.getContent_title());
                contentSession.setContent_brief_description(content.getContent_brief_description());
                contentSession.setContent_detailed_description(content.getContent_detailed_description());
                contentSession.setContent_status(String.valueOf(content.getContent_status()));
                contentSession.setId(stIndex);
                contentSessionRepository.deleteById(stIndex);
                contentSessionRepository.save(contentSession);

                //ELASTICSEARCH
                ContentElasticsearch contentElasticsearch = new ContentElasticsearch();
                contentElasticsearch.setContent_title(content.getContent_title());
                contentElasticsearch.setContent_brief_description(content.getContent_brief_description());
                contentElasticsearch.setContent_detailed_description(content.getContent_detailed_description());
                contentElasticsearch.setContent_status(String.valueOf(content.getContent_status()));
                contentElasticsearch.setId(stIndex);
                contentElasticRepository.deleteById(stIndex);
                contentElasticRepository.save(contentElasticsearch);
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error: " + ex);
                model.addAttribute("isError", 1);
                return rvalue + "contentupdate";
            }
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "contentupdate";
        }
        return "redirect:/admin/content/" + stIndex;
    }


    //update sayfasını açmak
    @GetMapping("/{stIndex}")
    public String contentUpdate(@PathVariable String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<Content> optionalContent = contentRepository.findById(index);
            if (optionalContent.isPresent()) {
                model.addAttribute("content", optionalContent.get());
                model.addAttribute("index", index);
                model.addAttribute("isError", 0);
                return "adminpanel/content/contentupdate";
            } else {
                return "error/404";
            }

        } catch (Exception e) {
            return "error/404";
        }
    }
}


