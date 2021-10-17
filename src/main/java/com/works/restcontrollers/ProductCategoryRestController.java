package com.works.restcontrollers;

import com.works.repositories._elastic.ProductCategoryElasticRepository;
import com.works.repositories._jpa.ProductCategoryRepository;
import com.works.repositories._redis.ProductCategorySessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/product/category")
public class ProductCategoryRestController {

    final ProductCategorySessionRepository productCategorySessionRepository;
    final ProductCategoryElasticRepository productCategoryElasticRepository;
    final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryRestController(ProductCategorySessionRepository productCategorySessionRepository, ProductCategoryElasticRepository productCategoryElasticRepository, ProductCategoryRepository productCategoryRepository) {
        this.productCategorySessionRepository = productCategorySessionRepository;
        this.productCategoryElasticRepository = productCategoryElasticRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    //Elasticsearch
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> productCategoryListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Basarili");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, productCategoryElasticRepository.findByPr_title(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex)-1, Util.pageSize)));
        Integer size = productCategoryElasticRepository.findByPr_title(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        // dogrulama
        System.out.println("******" + size + "-->" + stSearchKey + " " + Util.theCompany.getCompany_name());
        int additional = size % Util.pageSize == 0 ? 0 : 1;
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNTOFPAGE, size/Util.pageSize + additional);
        return hm;
    }

    //Redis
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> productCategoryList(@RequestBody @PathVariable String stIndex){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Basarili");
        hm.put(REnum.STATUS, true);
        if(stIndex.equals("0")){
            hm.put(REnum.RESULT, productCategorySessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        }else{
            hm.put(REnum.RESULT, productCategorySessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex)-1, Util.pageSize)));
        }
        int additional = (productCategorySessionRepository.count() % 10) == 0 ? 0 : 1;
        hm.put(REnum.COUNTOFPAGE, (productCategorySessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> productCategoryDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        productCategoryRepository.deleteById(Integer.valueOf(stIndex));
        productCategorySessionRepository.deleteById(stIndex);
        productCategoryElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

}
