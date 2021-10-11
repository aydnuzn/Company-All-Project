package com.works.controllers.adminpanel;

import com.works.entities.Advertisement;
import com.works.repositories._jpa.AdvertisementRepository;
import com.works.utils.Util;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.Valid;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Controller
@RequestMapping("/admin/advertisement")
public class AdvertisementController {

    final String rvalue = "adminpanel/advertisement/";
    final AdvertisementRepository advertisementRepository;

    public AdvertisementController(AdvertisementRepository advertisementRepository) {
        this.advertisementRepository = advertisementRepository;
    }

    @GetMapping("")
    public String advertisement(Model model) {
        model.addAttribute("advertisement", new Advertisement());
        model.addAttribute("isError", 0);
        return "adminpanel/advertisement/advertisementadd";
    }

    @GetMapping("/list")
    public String advertisementList() {
        return rvalue + "advertisementlist";
    }

    @PostMapping("/add")
    public String advertisementAdd(@Valid @ModelAttribute("advertisement") Advertisement advertisement, BindingResult bindingResult, Model model, @RequestPart(value = "adv_image_file", required = false) MultipartFile adv_image_file) {
        System.out.println(advertisement);
        if (!bindingResult.hasErrors()) {
            //Directory kurulu mu?
            File theDir = new File(Util.UPLOAD_DIR + "advertisement");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            //File İsmi oluşumu
            String fileName = StringUtils.cleanPath(adv_image_file.getOriginalFilename());
            String ext = "";
            try {//File kısmı validation'da kontrol edilmediği için resim yüklenmemesi durumu kontrolü
                int length = fileName.lastIndexOf(".");
                ext = fileName.substring(length, fileName.length());
            } catch (Exception e) {
                model.addAttribute("isError", 2);
                return rvalue + "advertisementadd";
            }
            String uui = UUID.randomUUID().toString();
            fileName = uui + ext;

            //File Kopyalama
            Path path = Paths.get(Util.UPLOAD_DIR + "advertisement/" + fileName);
            String height = "";
            String width = "";
            try {
                Files.copy(adv_image_file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
                BufferedImage image = ImageIO.read(adv_image_file.getInputStream());
                height = String.valueOf(image.getHeight());
                width = String.valueOf(image.getWidth());
                advertisement.setAdv_image(fileName);
            } catch (IOException e) {
                System.err.println(e);
            }
            if (height.equals(advertisement.getAdv_height()) && width.equals(advertisement.getAdv_width())) {
                advertisementRepository.save(advertisement);
            } else {
                model.addAttribute("isError", 1);
                return rvalue + "advertisementadd";
            }
            return "redirect:/admin/advertisement";
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "advertisementadd";
        }
    }
}
