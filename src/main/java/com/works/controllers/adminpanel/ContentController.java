package com.works.controllers.adminpanel;

import com.works.entities.Content;
import com.works.models._elastic.ContentElasticsearch;
import com.works.models._redis.ContentSession;
import com.works.repositories._elastic.ContentElasticRepository;
import com.works.repositories._jpa.ContentRepository;
import com.works.repositories._redis.ContentSessionRepository;
import com.works.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

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
}


