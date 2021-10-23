package com.works.restcontrollers;

import com.works.entities.security.User;
import com.works.models._elastic.CustomerElastic;
import com.works.models._redis.CustomerSession;
import com.works.repositories._elastic.CustomerElasticRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.CustomerSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin/customer")
public class CustomerRestController {
    final CustomerElasticRepository customerElasticRepository;
    final CustomerSessionRepository customerSessionRepository;
    final UserRepository userRepository;

    public CustomerRestController(CustomerElasticRepository customerElasticRepository, CustomerSessionRepository customerSessionRepository, UserRepository userRepository) {
        this.customerElasticRepository = customerElasticRepository;
        this.customerSessionRepository = customerSessionRepository;
        this.userRepository = userRepository;
    }

    //******************************* REST API *********************************
    //ELASTIC
    @GetMapping("/list/{stSearchKey}/{stIndex}")
    public Map<REnum, Object> customerListSearch(@RequestBody @PathVariable String stSearchKey, @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        hm.put(REnum.RESULT, customerElasticRepository.findByName(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        int additional = 0;
        Integer size = customerElasticRepository.findByName(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        // dogrulama icin
        System.out.println("*********" + size + "-->" + stSearchKey + " " + Util.theCompany.getCompany_name());
        if (size % Util.pageSize != 0) {
            additional = 1;
        }
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNTOFPAGE, size / Util.pageSize + additional);
        return hm;
    }

    //REDIS
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> customerList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        if (stIndex.equals("0")) {
            hm.put(REnum.RESULT, customerSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        } else {
            hm.put(REnum.RESULT, customerSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }
        int additional = 0;
        Integer totalSize = customerSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        if (totalSize % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (totalSize / Util.pageSize) + additional);
        return hm;
    }

    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> customerDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        userRepository.deleteById(Integer.valueOf(stIndex));
        customerSessionRepository.deleteById(stIndex);
        customerElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

    // *************************** Mvc-Pageable ***********************************
    //ELASTIC-DataTable
    @GetMapping("/datatable/list/{stSearchKey}")
    public Map<REnum, Object> customerPageListSearch(HttpServletRequest request, @PathVariable String stSearchKey) {
        Map<String, String[]> allMap = request.getParameterMap();

        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);

        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);
        hm.put(REnum.RESULT, (customerElasticRepository.findByName(stSearchKey + " " + Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0])))).getContent());
        Integer totalCount = customerElasticRepository.findByName(stSearchKey + " " + Util.theCompany.getCompany_name()).size();
        System.out.println("TOTAL -->" + totalCount);
        hm.put(REnum.ERROR, null);
        hm.put(REnum.COUNT, totalCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

    //REDIS-DataTable
    @GetMapping("/datatable/list")
    public Map<REnum, Object> customerPageList(HttpServletRequest request) {
        Map<String, String[]> allMap = request.getParameterMap();
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.STATUS, true);
        hm.put(REnum.MESSAGE, "Başarılı");
        int validPage = Integer.parseInt(allMap.get("start")[0]) == 0 ? 0 : (Integer.parseInt(allMap.get("start")[0])) / Integer.parseInt(allMap.get("length")[0]);

        hm.put(REnum.RESULT, customerSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name(), PageRequest.of(validPage, Integer.parseInt(allMap.get("length")[0]))));
        int filterCount = customerSessionRepository.findByCompanynameEquals(Util.theCompany.getCompany_name()).size();
        hm.put(REnum.COUNT, filterCount);
        hm.put(REnum.DRAW, Integer.parseInt(allMap.get("draw")[0]));
        return hm;
    }

    @PutMapping("/changeBan/{stIndex}")
    public Map<REnum, Object> changeBan(@PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        try {
            Optional<User> optCustomer = userRepository.findById(Integer.valueOf(stIndex));
            if (optCustomer.isPresent()) {
                if (optCustomer.get().getRoles().get(0).getRo_id() == 3) {
                    Optional<CustomerSession> optionalCustomerSession = customerSessionRepository.findById(stIndex);
                    Optional<CustomerElastic> optionalCustomerElastic = customerElasticRepository.findById(stIndex);
                    if (optCustomer.get().getCu_status() == 1) {
                        optCustomer.get().setCu_status(2);
                        optionalCustomerSession.get().setCu_status(2);
                        optionalCustomerElastic.get().setCu_status(2);
                    } else if (optCustomer.get().getCu_status() == 2) {
                        optCustomer.get().setCu_status(1);
                        optionalCustomerSession.get().setCu_status(1);
                        optionalCustomerElastic.get().setCu_status(1);
                    }
                    userRepository.saveAndFlush(optCustomer.get());

                    //redis
                    customerSessionRepository.deleteById(stIndex);
                    customerSessionRepository.save(optionalCustomerSession.get());

                    //elastic
                    customerElasticRepository.deleteById(stIndex);
                    customerElasticRepository.save(optionalCustomerElastic.get());
                    hm.put(REnum.MESSAGE, "Başarılı");
                    hm.put(REnum.STATUS, true);
                    return hm;
                }
            }
        } catch (Exception e) {
        }
        hm.put(REnum.MESSAGE, "Başarısız");
        hm.put(REnum.STATUS, false);
        return hm;
    }
}