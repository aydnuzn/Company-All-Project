package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.ProductRestControllerBusiness;
import com.works.utils.REnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@RequestMapping("/rest/admin/product")
@Api(value = "Product Rest Controller", authorizations = {@Authorization(value = "basicAuth")})
public class ProductRestController {

    final ProductRestControllerBusiness business;

    public ProductRestController(ProductRestControllerBusiness business) {
        this.business = business;
    }

    @ApiOperation(value = "Son Eklenen 10 Urun Getirme Servisi")
    @GetMapping("/list")
    public Map<REnum, Object> productLastTenItem() {
        return business.productLastTenItem();
    }

    // Madde 2 --> List/2/5 -> 2.sayfandan başlayıp 5 adet ürün getirecek
    // default olarak 1 sayfada 10 veri gosterildi
    //Redis
    @ApiOperation(value = "N. sayfadan başlayıp, K adet Ürün Getirme Servisi")
    @GetMapping("/list/{stPage}/{stIndex}")
    public Map<REnum, Object> productEntryQuantityList(@RequestBody @PathVariable String stPage, @PathVariable String stIndex) {
        return business.productEntryQuantityList(stPage, stIndex);
    }

    // Madde 3 --> ListCat/10/2/5 -> 10.nolu kategorinin 2.sayfandan başlayıp 5 adet ürün getirecek
    // Default olarak 1 sayfaya 10 deger geldigi varsayilmistir
    @ApiOperation(value = "N nolu Kategorinin K. sayfasından başlayıp L adet Ürün Getirme Servisi")
    @GetMapping("/listcategory/{stCategory}/{stPage}/{stIndex}")
    public Map<REnum, Object> productListByCategory(@RequestBody @PathVariable String stCategory, @PathVariable String stPage, @PathVariable String stIndex) {
        return business.productListByCategory(stCategory, stPage, stIndex);
    }

    // Madde 4 --> ListSearch/ali arçelik buzdolabı almak istiyor/1/5 -> Elastic Search ile arama yapılarak bu veri sayfala aile getirilecek.
    //Elasticsearch
    @ApiOperation(value = "(Elasticsearch) Arama Yapılarak Urun Getirme Servisi")
    @GetMapping("/listsearch/{stSearchKey}/{stPageIndex}/{stPageSize}")
    public Map<REnum, Object> productListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stPageIndex, @PathVariable String stPageSize) {
        return business.productListSearch(stSearchKey, stPageIndex, stPageSize);
    }

    // Madde 5 --> Belirtilen urunun ayrıntılı servisi
    @ApiOperation(value = "Detaylı Urun Getirme Servisi")
    @GetMapping("/detail/{stIndex}")
    public Map<REnum, Object> productDetail(@RequestBody @PathVariable String stIndex) {
        return business.productDetail(stIndex);
    }
    //**************************************************************************
/*
    //Redis
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> productList(@RequestBody @PathVariable String stIndex){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int size = productSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, size);
        hm.put(REnum.COUNTOFPAGE, ((int)Math.ceil((double)size/Util.pageSize)));
        hm.put(REnum.RESULT, productSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        hm.put(REnum.ERROR, null);
        return hm;
    }
*/

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> productDelete(@RequestBody @PathVariable String stIndex) {
        return business.productDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> productPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.productPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> productPageList(HttpServletRequest request) {
        return business.productPageList(request);
    }
}
