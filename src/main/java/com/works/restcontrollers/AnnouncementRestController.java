package com.works.restcontrollers;

import com.works.repositories._elastic.AnnouncementElasticRepository;
import com.works.repositories._jpa.AnnouncementRepository;
import com.works.repositories._redis.AnnouncementSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/announcement")
public class AnnouncementRestController {

    final AnnouncementElasticRepository announcementElasticRepository;
    final AnnouncementSessionRepository announcementSessionRepository;
    final AnnouncementRepository announcementRepository;

    public AnnouncementRestController(AnnouncementElasticRepository announcementElasticRepository, AnnouncementSessionRepository announcementSessionRepository, AnnouncementRepository announcementRepository) {
        this.announcementElasticRepository = announcementElasticRepository;
        this.announcementSessionRepository = announcementSessionRepository;
        this.announcementRepository = announcementRepository;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> announcementListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, announcementElasticRepository.findByAnn_title(stSearchKey +" "+ Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = announcementElasticRepository.findByAnn_title(stSearchKey +" "+ Util.theCompany.getCompany_name()).size();
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
    public Map<REnum, Object> announcementList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        if(stIndex.equals("0")){
            hm.put(REnum.RESULT, announcementSessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        }else{
            hm.put(REnum.RESULT, announcementSessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }
        int additional = 0;
        if (announcementSessionRepository.count() % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (announcementSessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> announcementDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        announcementRepository.deleteById(Integer.valueOf(stIndex));
        announcementSessionRepository.deleteById(stIndex);
        announcementElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }



}
