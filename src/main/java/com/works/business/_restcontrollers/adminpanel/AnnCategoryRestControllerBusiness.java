package com.works.business._restcontrollers.adminpanel;

import com.works.repositories._elastic.AnnCategoryElasticRepository;
import com.works.repositories._jpa.AnnCategoryRepository;
import com.works.repositories._redis.AnnCategorySessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class AnnCategoryRestControllerBusiness {
    final AnnCategoryElasticRepository annCategoryElasticRepository;
    final AnnCategorySessionRepository annCategorySessionRepository;
    final AnnCategoryRepository annCategoryRepository;

    public AnnCategoryRestControllerBusiness(AnnCategoryElasticRepository annCategoryElasticRepository, AnnCategorySessionRepository annCategorySessionRepository, AnnCategoryRepository annCategoryRepository) {
        this.annCategoryElasticRepository = annCategoryElasticRepository;
        this.annCategorySessionRepository = annCategorySessionRepository;
        this.annCategoryRepository = annCategoryRepository;
    }

    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> annCategoryListSearch(String stSearchKey, String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        Integer size = annCategoryElasticRepository.findByAnn_category_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, size);
        hm.put(REnum.COUNTOFPAGE, ((int) Math.ceil((double) size / Util.pageSize)));
        hm.put(REnum.RESULT, annCategoryElasticRepository.findByAnn_category_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        hm.put(REnum.ERROR, null);
        return hm;
    }

    public Map<REnum, Object> annCategoryList(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int size = annCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, size);
        hm.put(REnum.COUNTOFPAGE, ((int) Math.ceil((double) size / Util.pageSize)));
        hm.put(REnum.RESULT, annCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        return hm;
    }

    public Map<REnum, Object> annCategoryDelete(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        annCategoryRepository.deleteById(Integer.valueOf(stIndex));
        annCategorySessionRepository.deleteById(stIndex);
        annCategoryElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        return hm;
    }

    public Map<REnum, Object> annCategoryPageListSearch(HttpServletRequest request, String stSearchKey) {
        Map<String, String[]> allMap = request.getParameterMap();

        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");

        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, (annCategoryElasticRepository.findByAnn_category_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0])))).getContent());
        Integer totalCount = annCategoryElasticRepository.findByAnn_category_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNT, totalCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

    public Map<REnum, Object> annCategoryPageList(HttpServletRequest request) {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);

        hm.put(REnum.RESULT, annCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        int filterCount = annCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, filterCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

}
