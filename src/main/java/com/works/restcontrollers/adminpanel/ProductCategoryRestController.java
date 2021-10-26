package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.ProductCategoryRestControllerBusiness;
import com.works.entities.categories.ProductCategory;
import com.works.properties.ProductTopSubCategory;
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


    final ProductCategoryRestControllerBusiness business;

    public ProductCategoryRestController(ProductCategoryRestControllerBusiness business) {
        this.business = business;
    }

    @ApiOperation(value = "Ürün Kategori Getirme Servisi, Ana kategori ve Alt kategorisi şeklinde")
    @GetMapping("/list")
    public Map<REnum, Object> productTopSubCategoryList() {
        return business.productTopSubCategoryList();
    }

    //Elasticsearch
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> productCategoryListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.productCategoryListSearch(stSearchKey, stIndex);
    }

    //Redis
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> productCategoryList(@RequestBody @PathVariable String stIndex) {
        return business.productCategoryList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> productCategoryDelete(@RequestBody @PathVariable String stIndex) {
        return business.productCategoryDelete(stIndex);
    }

}
