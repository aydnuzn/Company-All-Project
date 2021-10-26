package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.SurveyRestControllerBusiness;
import com.works.utils.REnum;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/survey")
public class SurveyRestController {


    final SurveyRestControllerBusiness business;

    public SurveyRestController(SurveyRestControllerBusiness business) {
        this.business = business;
    }

    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> surveyListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.surveyListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> surveyList(@RequestBody @PathVariable String stIndex) {
        return business.surveyList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> surveyDelete(@RequestBody @PathVariable String stIndex) {
        return business.surveyDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> surveyPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.surveyPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> surveyPageList(HttpServletRequest request) {
        return business.surveyPageList(request);
    }

    //Oy verme
    @GetMapping("/vote/{stCustomer}/{stSurvey}/{stSelection}")
    public Map<REnum, Object> vote(@PathVariable String stCustomer, @PathVariable String stSurvey, @PathVariable String stSelection) {
        return business.vote(stCustomer, stSurvey, stSelection);
    }
}