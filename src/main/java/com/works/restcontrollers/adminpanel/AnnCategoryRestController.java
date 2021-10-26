package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.AnnCategoryRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/announcement/category")
public class AnnCategoryRestController {

    final AnnCategoryRestControllerBusiness business;

    public AnnCategoryRestController(AnnCategoryRestControllerBusiness business) {
        this.business = business;
    }

    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> annCategoryListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.annCategoryListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> annCategoryList(@RequestBody @PathVariable String stIndex) {
        return business.annCategoryList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> annCategoryDelete(@RequestBody @PathVariable String stIndex) {
        return business.annCategoryDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> annCategoryPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.annCategoryPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> annCategoryPageList(HttpServletRequest request) {
        return business.annCategoryPageList(request);
    }

}
