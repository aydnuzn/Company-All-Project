package com.works.controllers.adminpanel;

import com.works.entities.Gallery;
import com.works.entities.images.GalleryImage;
import com.works.properties.GalleryImageInterlayer;
import com.works.repositories._jpa.GalleryImageRepository;
import com.works.repositories._jpa.GalleryRepository;
import com.works.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/admin/gallery")
public class GalleryController {

    final String rvalue = "adminpanel/gallery/";
    final GalleryRepository galleryRepository;
    final GalleryImageRepository galleryImageRepository;

    public GalleryController(GalleryRepository galleryRepository, GalleryImageRepository galleryImageRepository) {
        this.galleryRepository = galleryRepository;
        this.galleryImageRepository = galleryImageRepository;
    }

    @GetMapping("")
    public String galleryAddShow(Model model){
        model.addAttribute("gallery", new Gallery());
        return "adminpanel/gallery/galleryadd";
    }

    @GetMapping("/list")
    public String galleryList(Model model) {
        model.addAttribute("ls", galleryRepository.getGalleryInfo(Util.theCompany.getId()));
        return "adminpanel/gallery/gallerylist";
    }

    @PostMapping("/add")
    public String galleryAdd(@Valid @ModelAttribute("gallery") Gallery gallery, BindingResult bindingResult, Model model){
        if(!bindingResult.hasErrors()){
            galleryRepository.save(gallery);
        }else{
            System.err.println(Util.errors(bindingResult));
        }
        return rvalue + "galleryadd";
    }

    @GetMapping("/image")
    public String galleryImageAddShow(Model model){
        model.addAttribute("ls", galleryRepository.findByCompany_IdEquals(Util.theCompany.getId()));
        model.addAttribute("galleryImageInterlayer", new GalleryImageInterlayer());
        model.addAttribute("isError",0);
        return "adminpanel/gallery/galleryimageadd";
    }

    @PostMapping("/image/add")
    public String galleryImageAdd(@Valid @ModelAttribute("galleryImageInterlayer") GalleryImageInterlayer galleryImageInterlayer, BindingResult bindingResult, Model model){
        System.out.println(bindingResult);
        if(!bindingResult.hasErrors()){
            Optional<Gallery> optionalGallery = galleryRepository.findById(galleryImageInterlayer.getGallery_title());
            GalleryImage galleryImage = new GalleryImage();
            galleryImage.setGallery(optionalGallery.get());
            galleryImage.setGallery_image_title(galleryImageInterlayer.getGallery_image_title());

            String fileName = StringUtils.cleanPath(galleryImageInterlayer.getGallery_image_files().getOriginalFilename());
            String ext = "";
            try {
                int length = fileName.lastIndexOf(".");
                ext = fileName.substring(length, fileName.length());
            } catch (Exception e) {
                model.addAttribute("ls", galleryRepository.findByCompany_IdEquals(Util.theCompany.getId()));
                model.addAttribute("galleryImageInterlayer", new GalleryImageInterlayer());
                model.addAttribute("isError",1);
                return "adminpanel/gallery/galleryimageadd";
            }
            String uui = UUID.randomUUID().toString();
            fileName = uui + ext;
            galleryImage.setGallery_image_url(fileName);
            try {
                galleryImageRepository.save(galleryImage);

                File theDir = new File(Util.UPLOAD_DIR + "gallery/" + optionalGallery.get().getId());
                if (!theDir.exists()) {
                    theDir.mkdirs();
                }
                Path path = Paths.get(Util.UPLOAD_DIR + "gallery/" + optionalGallery.get().getId() + "/" + fileName);
                Files.copy(galleryImageInterlayer.getGallery_image_files().getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                model.addAttribute("ls", galleryRepository.findByCompany_IdEquals(Util.theCompany.getId()));
                model.addAttribute("isError",0);
                return "adminpanel/gallery/galleryimageadd";
            }
            return "redirect:/admin/gallery/image";
        }else{
            System.err.println(Util.errors(bindingResult));
            model.addAttribute("ls", galleryRepository.findByCompany_IdEquals(Util.theCompany.getId()));
            model.addAttribute("isError",0);
            return rvalue + "galleryimageadd";
        }
    }






}
