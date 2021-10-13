package com.works.restcontrollers;

import com.works.repositories._elastic.ContentElasticRepository;
import com.works.repositories._jpa.ContentRepository;
import com.works.repositories._redis.ContentSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/content")
public class ContentRestController {

    final ContentRepository contentRepository;
    final ContentSessionRepository contentSessionRepository;
    final ContentElasticRepository contentElasticRepository;

    public ContentRestController(ContentRepository contentRepository, ContentSessionRepository contentSessionRepository, ContentElasticRepository contentElasticRepository) {
        this.contentRepository = contentRepository;
        this.contentSessionRepository = contentSessionRepository;
        this.contentElasticRepository = contentElasticRepository;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> contentListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, contentElasticRepository.findByContent_title(stSearchKey, PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = contentElasticRepository.findByContent_title(stSearchKey).size();
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
        hm.put(REnum.RESULT, contentSessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        if (contentSessionRepository.count() % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (contentSessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> surveyDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        contentRepository.deleteById(Integer.valueOf(stIndex));
        contentSessionRepository.deleteById(stIndex);
        contentElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }


}
