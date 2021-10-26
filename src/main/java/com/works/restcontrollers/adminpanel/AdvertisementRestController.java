package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.AdvertisementRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/advertisement")
public class AdvertisementRestController {

    final AdvertisementRestControllerBusiness business;

    public AdvertisementRestController(AdvertisementRestControllerBusiness business) {
        this.business = business;
    }

    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> advertisementListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.advertisementListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> advertisementList(@RequestBody @PathVariable String stIndex) {
        return business.advertisementList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> advertisementDelete(@RequestBody @PathVariable String stIndex) {
        return business.advertisementDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> advertisementPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.advertisementPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> advertisementPageList(HttpServletRequest request) {
        return business.advertisementPageList(request);
    }

}
