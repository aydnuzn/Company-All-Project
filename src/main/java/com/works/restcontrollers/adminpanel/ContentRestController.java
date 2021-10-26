package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.ContentRestControllerBusiness;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/content")
public class ContentRestController {

    final ContentRestControllerBusiness business;

    public ContentRestController(ContentRestControllerBusiness business) {
        this.business = business;
    }

    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> contentListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.contentListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> contentList(@RequestBody @PathVariable String stIndex) {
        return business.contentList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> contentDelete(@RequestBody @PathVariable String stIndex) {
        return business.contentDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> contentPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.contentPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> contentPageList(HttpServletRequest request) {
        return business.contentPageList(request);
    }

}
