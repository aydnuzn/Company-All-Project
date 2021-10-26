package com.works.restcontrollers;

import com.works.entities.categories.ProductCategory;
import com.works.properties.ProductTopSubCategory;
import com.works.repositories._elastic.ProductCategoryElasticRepository;
import com.works.repositories._jpa.ProductCategoryRepository;
import com.works.repositories._redis.ProductCategorySessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/product/category")
@Api(value = "ProductCategory Rest Controller", authorizations = {@Authorization(value = "basicAuth")})
public class ProductCategoryRestController {

    final ProductCategorySessionRepository productCategorySessionRepository;
    final ProductCategoryElasticRepository productCategoryElasticRepository;
    final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryRestController(ProductCategorySessionRepository productCategorySessionRepository, ProductCategoryElasticRepository productCategoryElasticRepository, ProductCategoryRepository productCategoryRepository) {
        this.productCategorySessionRepository = productCategorySessionRepository;
        this.productCategoryElasticRepository = productCategoryElasticRepository;
        this.productCategoryRepository = productCategoryRepository;
    }

    @ApiOperation(value = "Ürün Kategori Getirme Servisi, Ana kategori ve Alt kategorisi şeklinde")
    @GetMapping("/list")
    public Map<REnum, Object> productTopSubCategoryList(){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        List<ProductCategory> productCategoryList = productCategoryRepository.findByCompany_IdEquals(Util.theCompany.getId());
        if(productCategoryList.size()>0){
            List<ProductTopSubCategory> productTopSubCategoryList = new ArrayList<>();
            List<String> subCategoryList = new ArrayList<>();
            Boolean control = false;
            ProductTopSubCategory productTopSubCategory = null;
            for (int i = 0; i < productCategoryList.size(); i++) {
                control = false;
                ProductCategory itm = productCategoryList.get(i);
                try{
                    if(itm.getProductCategories().getId() == null){
                        System.out.println("Ana katagori bulundu");
                    }
                }catch(Exception ex){
                    productTopSubCategory = new ProductTopSubCategory();
                    productTopSubCategory.setTop_title(itm.getPr_title());
                    control = true;
                }
                for (int j = 0; j < productCategoryList.size(); j++) {
                    ProductCategory itm2 = productCategoryList.get(j);
                    try{
                        if(itm.getId() == itm2.getProductCategories().getId()){
                            subCategoryList.add(itm2.getPr_title());
                        }
                    }catch(Exception ex){}
                }
                if(control){
                    productTopSubCategory.setSub_titles(subCategoryList);
                    productTopSubCategoryList.add(productTopSubCategory);
                    subCategoryList = new ArrayList<>();
                }
            }
            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "İşlem Başarılı");
            hm.put(REnum.RESULT, productTopSubCategoryList);
        }else{
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Veritabanında kayıt yok");
            hm.put(REnum.RESULT, new ProductTopSubCategory());
        }
        return hm;
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
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        if (stIndex.equals("0")) {
            hm.put(REnum.RESULT, productCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        } else {
            hm.put(REnum.RESULT, productCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }
        int additional = 0;
        Integer totalSize = productCategorySessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        if (totalSize % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (totalSize / Util.pageSize) + additional);
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
