package com.works.restcontrollers;

import com.works.entities.Gallery;
import com.works.entities.constant.address.District;
import com.works.entities.images.GalleryImage;
import com.works.repositories._jpa.DistrictRepository;
import com.works.repositories._jpa.GalleryImageRepository;
import com.works.repositories._jpa.GalleryRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class ConstantRestController {
    final DistrictRepository districtRepository;
    final GalleryRepository galleryRepository;
    final GalleryImageRepository galleryImageRepository;

    public ConstantRestController(DistrictRepository districtRepository, GalleryRepository galleryRepository, GalleryImageRepository galleryImageRepository) {
        this.districtRepository = districtRepository;
        this.galleryRepository = galleryRepository;
        this.galleryImageRepository = galleryImageRepository;
    }

    @GetMapping("/getXDistricts/{stIndex}")
    public Map<REnum, Object> getXDistricts(@PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        List<District> districtList = new ArrayList<>();
        try {
            int did = Integer.parseInt(stIndex);
            districtList = districtRepository.findByCity_idEquals(did);
            if (districtList.size() > 0) {
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "İlçeler bulundu.");
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Veri tabanında" + did + " numaralı İlçe bulunamadı.");
            }
        } catch (Exception ex) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "İlçe bulunamadı. - Integer ifade girilmesi gerekir.");
        }
        hm.put(REnum.COUNT, districtList.size());
        hm.put(REnum.RESULT, districtList);
        return hm;
    }

    @GetMapping("/getLogo")
    public Map<String, Object> getLogo() {
        Map<String, Object> hm = new LinkedHashMap<>();
        hm.put("company", Util.theCompany);
        return hm;
    }

    @DeleteMapping("/delete/gallery/{stIndex}")
    public Map<REnum, Object> galleryDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try{
            Integer index = Integer.parseInt(stIndex);
            Optional<Gallery> optionalGallery = galleryRepository.findById(index);
            if(optionalGallery.isPresent()){
                try{
                    galleryRepository.deleteById(index);
                    hm.put(REnum.STATUS, true);
                    hm.put(REnum.MESSAGE, "İşlem Başarılı.");
                }catch (Exception ex){
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "İşlem Başarısız. Silinmek istenen değer, başka toblolar tarafından kullanılmaktadır.");
                }
            }else{
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Silinmek istenen galeri mevcut değil");
            }
        }catch (Exception ex){
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "String deger girilmiş. Silme basarisiz");
        }
        return hm;
    }

    @DeleteMapping("/delete/gallery/{galleryId}/{imageId}")
    public Map<REnum, Object> galleryImageDelete(@RequestBody @PathVariable String galleryId, @PathVariable String imageId) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try{
            Integer image_id = Integer.parseInt(imageId);
            Integer gallery_id = Integer.parseInt(galleryId);
            Optional<Gallery> optionalGallery = galleryRepository.findByGalleryImages_IdEquals(image_id);
            if (optionalGallery.get().getId().equals(gallery_id)){
                Optional<GalleryImage> optionalGalleryImage = galleryImageRepository.findById(image_id);
                if(optionalGalleryImage.isPresent()){
                    galleryImageRepository.deleteById(image_id);
                    hm.put(REnum.STATUS, true);
                    hm.put(REnum.MESSAGE, "İşlem Başarılı.");
                }else{
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "Silinmek istenen resim mevcut değil");
                }
            }else{
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Silinmek istenen resim, giriş yapilan kategoriye ait değildir.");
            }
        }catch (Exception ex){
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "String deger girilmiş. Silme basarisiz");
        }
        return hm;
    }
}
