package com.works.business._restcontrollers.adminpanel;

import com.works.entities.security.Role;
import com.works.entities.security.User;
import com.works.models._elastic.CustomerElastic;
import com.works.models._redis.CustomerSession;
import com.works.properties.CustomerInterlayer;
import com.works.repositories._elastic.CustomerElasticRepository;
import com.works.repositories._jpa.RoleRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.CustomerSessionRepository;
import com.works.services.UserService;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class CustomerRestControllerBusiness {
    final CustomerElasticRepository customerElasticRepository;
    final CustomerSessionRepository customerSessionRepository;
    final UserRepository userRepository;
    final UserService userService;
    final RoleRepository roleRepository;

    public CustomerRestControllerBusiness(CustomerElasticRepository customerElasticRepository, CustomerSessionRepository customerSessionRepository, UserRepository userRepository, UserService userService, RoleRepository roleRepository) {
        this.customerElasticRepository = customerElasticRepository;
        this.customerSessionRepository = customerSessionRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    public Map<REnum, Object> customerListSearch(String stSearchKey, String stIndex) {
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

    public Map<REnum, Object> customerList(String stIndex) {
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

    public Map<REnum, Object> customerDelete(String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        userRepository.deleteById(Integer.valueOf(stIndex));
        customerSessionRepository.deleteById(stIndex);
        customerElasticRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }

    public Map<REnum, Object> customerPageListSearch(HttpServletRequest request, String stSearchKey) {
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

    public Map<REnum, Object> changeBan(String stIndex) {
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

    public Map<REnum, Object> customerAdd(CustomerInterlayer customerInterlayer, BindingResult bindingResult) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            User customer = new User();
            customer.setName(customerInterlayer.getCu_name());
            customer.setSurname(customerInterlayer.getCu_surname());
            customer.setEmail(customerInterlayer.getCu_email());
            customer.setPassword(userService.encoder().encode(customerInterlayer.getCu_password()));

            customer.setTel(customerInterlayer.getCu_tel());
            customer.setCu_status(customerInterlayer.getCu_status());
            customer.setEnabled(true);
            customer.setTokenExpired(true);
            customer.setAddresses(null);
            Optional<Role> optRole = roleRepository.findById(3);
            List<Role> roleList = new ArrayList<>();
            if (optRole.isPresent()) {
                roleList.add(optRole.get());
                customer.setRoles(roleList);
            } else {
                //Veritabanında Müşteri Rolü Eksik.
                customer.setRoles(null);
            }
            try {
                customer = userRepository.save(customer);
            } catch (DataIntegrityViolationException e) {
                if (userRepository.findByEmailEquals(customer.getEmail()).isPresent()) {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "E-Mail Mevcut! (Unique olmalı)");
                    return hm;
                } else {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "Telefon Mevcut! (Unique olmalı)");
                    return hm;
                }
            }

            CustomerSession customerSession = new CustomerSession();
            customerSession.setId(String.valueOf(customer.getId()));
            customerSession.setName(customer.getName());
            customerSession.setSurname(customer.getSurname());
            customerSession.setEmail(customer.getEmail());
            customerSession.setTel(customer.getTel());
            customerSession.setCu_status(customer.getCu_status());
            customerSessionRepository.save(customerSession);

            CustomerElastic customerElastic = new CustomerElastic();
            customerElastic.setId(String.valueOf(customer.getId()));
            customerElastic.setName(customer.getName());
            customerElastic.setSurname(customer.getSurname());
            customerElastic.setEmail(customer.getEmail());
            customerElastic.setTel(customer.getTel());
            customerElastic.setCu_status(customer.getCu_status());
            customerElasticRepository.save(customerElastic);
            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "İşlem başarılı");
            return hm;
        } else {
            System.out.println(Util.errors(bindingResult));
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Validasyona takıldı");
            hm.put(REnum.ERROR, "Valid hatası: " + bindingResult);
            return hm;
        }
    }

    public Map<REnum, Object> customerUpdate(CustomerInterlayer customerInterlayer, BindingResult bindingResult, String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        if (!bindingResult.hasErrors()) {
            User customer = new User();
            customer.setName(customerInterlayer.getCu_name());
            customer.setSurname(customerInterlayer.getCu_surname());
            customer.setEmail(customerInterlayer.getCu_email());
            customer.setPassword(userService.encoder().encode(customerInterlayer.getCu_password()));

            customer.setTel(customerInterlayer.getCu_tel());
            customer.setCu_status(customerInterlayer.getCu_status());
            customer.setEnabled(true);
            customer.setTokenExpired(true);
            customer.setAddresses(null);
            Optional<Role> optRole = roleRepository.findById(3);
            List<Role> roleList = new ArrayList<>();
            if (optRole.isPresent()) {
                roleList.add(optRole.get());
                customer.setRoles(roleList);
            } else {
                //Veritabanında Müşteri Rolü Eksik.
                customer.setRoles(null);
            }
            try {
                customer.setId(Integer.valueOf(stIndex));
                customer = userRepository.saveAndFlush(customer);
            } catch (DataIntegrityViolationException e) {
                if (userRepository.findByEmailEquals(customer.getEmail()).isPresent()) {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "E-Mail Mevcut! (Unique olmalı)");
                    return hm;
                } else {
                    hm.put(REnum.STATUS, false);
                    hm.put(REnum.MESSAGE, "Telefon Mevcut! (Unique olmalı)");
                    return hm;
                }
            }

            CustomerSession customerSession = new CustomerSession();
            customerSession.setId(String.valueOf(customer.getId()));
            customerSession.setName(customer.getName());
            customerSession.setSurname(customer.getSurname());
            customerSession.setEmail(customer.getEmail());
            customerSession.setTel(customer.getTel());
            customerSession.setCu_status(customer.getCu_status());
            customerSessionRepository.deleteById(stIndex);
            customerSessionRepository.save(customerSession);

            CustomerElastic customerElastic = new CustomerElastic();
            customerElastic.setId(String.valueOf(customer.getId()));
            customerElastic.setName(customer.getName());
            customerElastic.setSurname(customer.getSurname());
            customerElastic.setEmail(customer.getEmail());
            customerElastic.setTel(customer.getTel());
            customerElastic.setCu_status(customer.getCu_status());
            customerElasticRepository.deleteById(stIndex);
            customerElasticRepository.save(customerElastic);
            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "İşlem başarılı");
            return hm;
        } else {
            System.out.println(Util.errors(bindingResult));
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Validasyona takıldı");
            hm.put(REnum.ERROR, "Valid hatası: " + bindingResult);
            return hm;

        }
    }
}
