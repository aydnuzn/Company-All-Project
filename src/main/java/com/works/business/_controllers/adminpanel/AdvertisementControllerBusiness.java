package com.works.business._controllers.adminpanel;

import com.works.entities.Advertisement;
import com.works.models._elastic.AdvertisementElasticsearch;
import com.works.models._redis.AdvertisementSession;
import com.works.repositories._elastic.AdvertisementElasticRepository;
import com.works.repositories._jpa.AdvertisementRepository;
import com.works.repositories._redis.AdvertisementSessionRepository;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class AdvertisementControllerBusiness {
    final String rvalue = "adminpanel/advertisement/";
    final AdvertisementRepository advertisementRepository;
    final AdvertisementSessionRepository advertisementSessionRepository;
    final AdvertisementElasticRepository advertisementElasticRepository;

    public AdvertisementControllerBusiness(AdvertisementRepository advertisementRepository, AdvertisementSessionRepository advertisementSessionRepository, AdvertisementElasticRepository advertisementElasticRepository) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementSessionRepository = advertisementSessionRepository;
        this.advertisementElasticRepository = advertisementElasticRepository;
    }


    public String advertisement(Model model) {
        model.addAttribute("advertisement", new Advertisement());
        model.addAttribute("isError", 0);
        return rvalue + "advertisementadd";
    }

    public String advertisementList() {
        return rvalue + "advertisementlist";
    }

    public String advertisementUpdate(Advertisement advertisement, BindingResult bindingResult, Model model, String stIndex) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
        } catch (Exception e) {
            return "error/404";
        }
        if (!bindingResult.hasErrors()) {
            try {
                advertisement.setId(index);
                advertisementRepository.saveAndFlush(advertisement);
                AdvertisementSession advertisementSession = new AdvertisementSession();
                advertisementSession.setId(stIndex);
                advertisementSession.setAdv_title(advertisement.getAdv_title());
                advertisementSession.setAdv_link(advertisement.getAdv_link());
                advertisementSession.setAdv_height(advertisement.getAdv_height());
                advertisementSession.setAdv_width(advertisement.getAdv_width());
                advertisementSession.setAdv_date_end(String.valueOf(advertisement.getAdv_date_end()));
                advertisementSession.setAdv_date_begin(String.valueOf(advertisement.getAdv_date_begin()));
                advertisementSession.setAdv_shown_number(String.valueOf(advertisement.getAdv_shown_number()));
                advertisementSessionRepository.deleteById(stIndex);
                advertisementSessionRepository.save(advertisementSession);
                AdvertisementElasticsearch advertisementElasticsearch = new AdvertisementElasticsearch();
                advertisementElasticsearch.setAdv_title(advertisement.getAdv_title());
                advertisementElasticsearch.setAdv_shown_number(String.valueOf(advertisement.getAdv_shown_number()));
                advertisementElasticsearch.setAdv_date_begin(String.valueOf(advertisement.getAdv_date_begin()));
                advertisementElasticsearch.setAdv_date_end(String.valueOf(advertisement.getAdv_date_end()));
                advertisementElasticsearch.setAdv_image(advertisement.getAdv_image());
                advertisementElasticsearch.setAdv_width(advertisement.getAdv_width());
                advertisementElasticsearch.setAdv_height(advertisement.getAdv_height());
                advertisementElasticsearch.setAdv_link(advertisement.getAdv_link());
                advertisementElasticsearch.setId(stIndex);
                advertisementElasticRepository.deleteById(stIndex);
                advertisementElasticRepository.save(advertisementElasticsearch);
            } catch (DataIntegrityViolationException ex) {
                System.err.println("Error: " + ex);
                model.addAttribute("isError", 1);
                return rvalue + "advertisementupdate";
            }
        } else {
            System.out.println(Util.errors(bindingResult));
            model.addAttribute("isError", 0);
            return rvalue + "advertisementupdate";
        }
        return "redirect:/admin/advertisement/" + stIndex;
    }

    public String advertisementUpdate(String stIndex, Model model) {
        Integer index = 0;
        try {
            index = Integer.parseInt(stIndex);
            Optional<Advertisement> optionalAdvertisement = advertisementRepository.findById(index);
            if (optionalAdvertisement.isPresent()) {
                model.addAttribute("advertisement", optionalAdvertisement.get());
                model.addAttribute("index", index);
                model.addAttribute("isError", 0);
                return rvalue + "advertisementupdate";
            } else {
                return "error/404";
            }

        } catch (Exception e) {
            return "error/404";
        }
    }

    public String advertisementAdd(Advertisement advertisement, BindingResult bindingResult, Model model, MultipartFile adv_image_file) {
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
                String advId = String.valueOf(advertisementRepository.save(advertisement).getId());
                AdvertisementSession advertisementSession = new AdvertisementSession();
                advertisementSession.setAdv_title(advertisement.getAdv_title());
                advertisementSession.setAdv_shown_number(String.valueOf(advertisement.getAdv_shown_number()));
                advertisementSession.setAdv_date_begin(String.valueOf(advertisement.getAdv_date_begin()));
                advertisementSession.setAdv_date_end(String.valueOf(advertisement.getAdv_date_end()));
                advertisementSession.setAdv_image(advertisement.getAdv_image());
                advertisementSession.setAdv_width(advertisement.getAdv_width());
                advertisementSession.setAdv_height(advertisement.getAdv_height());
                advertisementSession.setAdv_link(advertisement.getAdv_link());
                advertisementSession.setId(advId);
                advertisementSessionRepository.save(advertisementSession);

                AdvertisementElasticsearch advertisementElasticsearch = new AdvertisementElasticsearch();
                advertisementElasticsearch.setAdv_title(advertisement.getAdv_title());
                advertisementElasticsearch.setAdv_shown_number(String.valueOf(advertisement.getAdv_shown_number()));
                advertisementElasticsearch.setAdv_date_begin(String.valueOf(advertisement.getAdv_date_begin()));
                advertisementElasticsearch.setAdv_date_end(String.valueOf(advertisement.getAdv_date_end()));
                advertisementElasticsearch.setAdv_image(advertisement.getAdv_image());
                advertisementElasticsearch.setAdv_width(advertisement.getAdv_width());
                advertisementElasticsearch.setAdv_height(advertisement.getAdv_height());
                advertisementElasticsearch.setAdv_link(advertisement.getAdv_link());
                advertisementElasticsearch.setId(advId);
                advertisementElasticRepository.save(advertisementElasticsearch);


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
