package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.GalleryControllerBusiness;
import com.works.entities.Gallery;
import com.works.entities.images.GalleryImage;
import com.works.properties.GalleryImageInterlayer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin/gallery")
public class GalleryController {

    final GalleryControllerBusiness business;

    public GalleryController(GalleryControllerBusiness business) {
        this.business = business;
    }


    @GetMapping("")
    public String galleryAddShow(Model model) {
        return business.galleryAddShow(model);
    }

    @GetMapping("/list")
    public String galleryList(Model model) {
        return business.galleryList(model);
    }

    @PostMapping("/add")
    public String galleryAdd(@Valid @ModelAttribute("gallery") Gallery gallery, BindingResult bindingResult, Model model) {
        return business.galleryAdd(gallery, bindingResult, model);
    }

    @GetMapping("/{stIndex}")
    public String galleryUpdateShow(@PathVariable String stIndex, Model model) {
        return business.galleryUpdateShow(stIndex, model);
    }

    @PostMapping("/update/{stIndex}")
    public String galleryUpdate(@Valid @ModelAttribute("gallery") Gallery gallery, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        return business.galleryUpdate(gallery, bindingResult, stIndex, model);
    }

    //ImagesShow
    @GetMapping("/images/{stIndex}")
    public String galleryImagesShow(@PathVariable String stIndex, Model model) {
        return business.galleryImagesShow(stIndex, model);
    }

    @GetMapping("/image")
    public String galleryImageAddShow(Model model) {
        return business.galleryImageAddShow(model);
    }

    @GetMapping("/imageupdate/{stIndex}")
    public String galleryImageUpdateShow(@PathVariable String stIndex, Model model) {
        return business.galleryImageUpdateShow(stIndex, model);
    }

    @PostMapping("/imageupdates/{stIndex}")
    public String galleryImageUpdate(@Valid @ModelAttribute("galleryImage") GalleryImage galleryImage, BindingResult bindingResult, @PathVariable String stIndex, Model model) {
        return business.galleryImageUpdate(galleryImage, bindingResult, stIndex, model);
    }

    //*************************************************************************

    @PostMapping("/image/add")
    public String galleryImageAdd(@Valid @ModelAttribute("galleryImageInterlayer") GalleryImageInterlayer galleryImageInterlayer, BindingResult bindingResult, Model model) {
        return business.galleryImageAdd(galleryImageInterlayer, bindingResult, model);
    }


}
