package com.works.restcontrollers;

import com.works.repositories._elastic.AdvertisementElasticRepository;
import com.works.repositories._jpa.AdvertisementRepository;
import com.works.repositories._redis.AdvertisementSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/advertisement")
public class AdvertisementRestController {

    final AdvertisementRepository advertisementRepository;
    final AdvertisementSessionRepository advertisementSessionRepository;
    final AdvertisementElasticRepository advertisementElasticRepository;

    public AdvertisementRestController(AdvertisementRepository advertisementRepository, AdvertisementSessionRepository advertisementSessionRepository, AdvertisementElasticRepository advertisementElasticRepository) {
        this.advertisementRepository = advertisementRepository;
        this.advertisementSessionRepository = advertisementSessionRepository;
        this.advertisementElasticRepository = advertisementElasticRepository;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> contentListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, advertisementElasticRepository.findByAdv_title(stSearchKey +" "+ Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = advertisementElasticRepository.findByAdv_title(stSearchKey +" "+ Util.theCompany.getCompany_name()).size();
        // dogrulama icin
        System.out.println("*********"+size + "-->" + stSearchKey +" "+ Util.theCompany.getCompany_name());
        if (size % Util.pageSize != 0) {
            additional = 1;
        }
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNTOFPAGE, size / Util.pageSize + additional);
        return hm;
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> contentList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, advertisementSessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        if (advertisementSessionRepository.count() % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (advertisementSessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> advDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        advertisementRepository.deleteById(Integer.valueOf(stIndex));
        advertisementSessionRepository.deleteById(stIndex);
        advertisementElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }


}
