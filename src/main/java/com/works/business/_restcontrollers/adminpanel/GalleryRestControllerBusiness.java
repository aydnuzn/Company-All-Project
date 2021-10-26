package com.works.business._restcontrollers.adminpanel;

import com.works.entities.projections.GalleryInfo;
import com.works.repositories._jpa.GalleryRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class GalleryRestControllerBusiness {
    final GalleryRepository galleryRepository;

    public GalleryRestControllerBusiness(GalleryRepository galleryRepository) {
        this.galleryRepository = galleryRepository;
    }

    public Map<REnum, Object> galleryList() {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "İşlem başarılı");
        hm.put(REnum.RESULT, galleryRepository.getGalleryInfo(Util.theCompany.getId()));
        return hm;
    }

    public Map<REnum, Object> galleryUpdateShow(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Integer index = Integer.parseInt(stIndex);
            List<GalleryInfo> list = galleryRepository.getGalleryInfoByCategory(index);
            if (list.size() > 0) {
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "İşlem başarılı");
                hm.put(REnum.RESULT, list);
                return hm;
            } else {

                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Görüntülenmek istenen galeri mevcut değil!");
                return hm;
            }
        } catch (Exception ex) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Url kismina String değer girilmiş, Integer değer girilmeli!");
            return hm;
        }
    }


}
