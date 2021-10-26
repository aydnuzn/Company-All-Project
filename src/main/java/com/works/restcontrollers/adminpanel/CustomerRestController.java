package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.CustomerRestControllerBusiness;
import com.works.properties.CustomerInterlayer;
import com.works.utils.REnum;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/rest/admin/customer")
public class CustomerRestController {
    final CustomerRestControllerBusiness business;

    public CustomerRestController(CustomerRestControllerBusiness business) {
        this.business = business;
    }


    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> customerListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        return business.customerListSearch(stSearchKey, stIndex);
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> customerList(@RequestBody @PathVariable String stIndex) {
        return business.customerList(stIndex);
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> customerDelete(@RequestBody @PathVariable String stIndex) {
        return business.customerDelete(stIndex);
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> customerPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        return business.customerPageListSearch(request, stSearchKey);
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> customerPageList(HttpServletRequest request) {
        return business.customerPageList(request);
    }

    @PutMapping("/changeBan/{stIndex}")
    public Map<REnum, Object> changeBan(@PathVariable String stIndex) {
        return business.changeBan(stIndex);
    }

    @PostMapping("/add")
    public Map<REnum, Object> customerAdd(@Valid @RequestBody CustomerInterlayer customerInterlayer, BindingResult bindingResult) {
        return business.customerAdd(customerInterlayer, bindingResult);
    }

    @PutMapping("/update/{stIndex}")
    public Map<REnum, Object> customerUpdate(@Valid @RequestBody CustomerInterlayer customerInterlayer, BindingResult bindingResult, @PathVariable String stIndex) {
        return business.customerUpdate(customerInterlayer, bindingResult, stIndex);
    }
}