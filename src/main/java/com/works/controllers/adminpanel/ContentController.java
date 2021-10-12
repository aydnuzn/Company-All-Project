package com.works.controllers.adminpanel;

import com.works.entities.Content;
import com.works.entities.survey.Survey;
import com.works.repositories._jpa.ContentRepository;
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

    public ContentController(ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @GetMapping("")
    public String content(Model model) {
        model.addAttribute("content", new Content());
        model.addAttribute("isError", 0);
        return "adminpanel/content/contentadd";
    }

    @GetMapping("/list")
    public String contentList() {
        return rvalue + "contentlist";
    }

    @PostMapping("/add")
    public String contentAdd(@Valid @ModelAttribute("content") Content content, BindingResult bindingResult, Model model) {
        if(!bindingResult.hasErrors()){
            try {
                contentRepository.save(content);
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
