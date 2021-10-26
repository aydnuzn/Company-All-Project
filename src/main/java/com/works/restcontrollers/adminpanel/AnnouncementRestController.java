package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.AnnouncementRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/announcement")
public class AnnouncementRestController {

    final AnnouncementRestControllerBusiness business;

    public AnnouncementRestController(AnnouncementRestControllerBusiness business) {
        this.business = business;
    }

    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> announcementListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.announcementListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> announcementList(@RequestBody @PathVariable String stIndex) {
        return business.announcementList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> announcementDelete(@RequestBody @PathVariable String stIndex) {
        return business.announcementDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> announcementPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.announcementPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> announcementPageList(HttpServletRequest request) {
        return business.announcementPageList(request);
    }


}
