package com.works.restcontrollers;

import com.works.repositories._elastic.SurveyElasticRepository;
import com.works.repositories._jpa.SurveyRepository;
import com.works.repositories._redis.SurveySessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/survey")
public class SurveyRestController {

    final SurveyRepository surveyRepository;
    final SurveySessionRepository surveySessionRepository;
    final SurveyElasticRepository surveyElasticRepository;

    public SurveyRestController(SurveySessionRepository surveySessionRepository, SurveyRepository surveyRepository, SurveyElasticRepository surveyElasticRepository) {
        this.surveySessionRepository = surveySessionRepository;
        this.surveyRepository = surveyRepository;
        this.surveyElasticRepository = surveyElasticRepository;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> surveyListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, surveyElasticRepository.findBySurvey_title(stSearchKey, PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = surveyElasticRepository.findBySurvey_title(stSearchKey).size();
        if (size % Util.pageSize != 0) {
            additional = 1;
        }
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNTOFPAGE, size / Util.pageSize + additional);
        return hm;
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> surveyList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, surveySessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        if (surveySessionRepository.count() % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (surveySessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    //Anket Silme
    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> surveyDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        surveyRepository.deleteById(Integer.valueOf(stIndex));
        surveySessionRepository.deleteById(stIndex);
        surveyElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }
}