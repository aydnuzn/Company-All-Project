package com.works.restcontrollers;

import com.works.repositories._elastic.AnnCategoryElasticRepository;
import com.works.repositories._jpa.AnnCategoryRepository;
import com.works.repositories._redis.AnnCategorySessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/announcement/category")
public class AnnCategoryRestController {

    final AnnCategoryElasticRepository annCategoryElasticRepository;
    final AnnCategorySessionRepository annCategorySessionRepository;
    final AnnCategoryRepository annCategoryRepository;

    public AnnCategoryRestController(AnnCategoryElasticRepository annCategoryElasticRepository, AnnCategorySessionRepository annCategorySessionRepository, AnnCategoryRepository annCategoryRepository) {
        this.annCategoryElasticRepository = annCategoryElasticRepository;
        this.annCategorySessionRepository = annCategorySessionRepository;
        this.annCategoryRepository = annCategoryRepository;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> annCategoryListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, annCategoryElasticRepository.findByAnn_category_title(stSearchKey +" "+ Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = annCategoryElasticRepository.findByAnn_category_title(stSearchKey +" "+ Util.theCompany.getCompany_name()).size();
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
    public Map<REnum, Object> annCategoryList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        if(stIndex.equals("0")){
            hm.put(REnum.RESULT, annCategorySessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        }else{
            hm.put(REnum.RESULT, annCategorySessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }

        int additional = 0;
        if (annCategorySessionRepository.count() % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (annCategorySessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> annCategoryDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        annCategoryRepository.deleteById(Integer.valueOf(stIndex));
        annCategorySessionRepository.deleteById(stIndex);
        annCategoryElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> annCategoryPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        Map<String, String[]> allMap = request.getParameterMap();

        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);

        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0:(Integer.parseInt(allMap.get("start")[0]))/Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, (annCategoryElasticRepository.findByAnn_category_title(stSearchKey +" "+ Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0])))).getContent());
        Integer totalCount = annCategoryElasticRepository.findByAnn_category_title(stSearchKey +" "+ Util.theCompany.getCompany_name()).size();
        System.out.println("TOTAL -->" + totalCount);
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNT, totalCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]) );
        return hm;
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> annCategoryPageList(HttpServletRequest request) {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0:(Integer.parseInt(allMap.get("start")[0]))/Integer.parseInt(allMap.get("length")[0]);

        hm.put(REnum.RESULT, annCategorySessionRepository.findByOrderByIdAsc(PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        //int filterCount = announcementSessionRepository.findByOrderByIdAsc(Util.theCompany.getCompany_name(),PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))).size();
        //hm.put(REnum.RESULT, announcementSessionRepository.findByOrderByIdAsc(Util.theCompany.getCompany_name(),PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        hm.put(REnum.COUNT, annCategorySessionRepository.count() );
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]) );
        return hm;
    }

}
