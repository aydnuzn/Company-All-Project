package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.ContentControllerBusiness;
import com.works.entities.Content;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/content")
public class ContentController {
    final ContentControllerBusiness business;

    public ContentController(ContentControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("")
    public String content(Model model) {
        return business.content(model);
    }

    @GetMapping("/list")
    public String contentList() {
        return business.contentList();
    }

    @PostMapping("/add")
    public String contentAdd(@Valid @ModelAttribute("content") Content content, BindingResult bindingResult, Model model) {
        return business.contentAdd(content, bindingResult, model);
    }

    @PostMapping("/update/{stIndex}")
    public String contentUpdate(@Valid @ModelAttribute("content") Content content, BindingResult bindingResult, Model model, @PathVariable String stIndex) {
        return business.contentUpdate(content, bindingResult, model, stIndex);
    }

    //update sayfasını açmak
    @GetMapping("/{stIndex}")
    public String contentUpdate(@PathVariable String stIndex, Model model) {
        return business.contentUpdate(stIndex, model);
    }
}


