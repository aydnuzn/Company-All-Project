package com.works.restcontrollers;

import com.works.entities.Orders;
import com.works.models._redis.OrderSession;
import com.works.repositories._jpa.OrderRepository;
import com.works.repositories._redis.OrderSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/orders")
public class OrdersRestController {

    final OrderRepository orderRepository;
    final OrderSessionRepository orderSessionRepository;

    public OrdersRestController(OrderRepository orderRepository, OrderSessionRepository orderSessionRepository) {
        this.orderRepository = orderRepository;
        this.orderSessionRepository = orderSessionRepository;
    }

    @PostMapping("/add")
    public Map<REnum, Object> orderAdd(@Valid Orders orders, BindingResult bindingResult){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        OrderSession orderSession = new OrderSession();
        if(!bindingResult.hasErrors()){
            orderSession.setProduct_id(String.valueOf(orders.getProduct().getId()));
            orderSession.setUser_id(String.valueOf(orders.getUser().getId()));
            orderSession.setCustomer_address(orders.getCustomer_address());
            orderSession.setOrder_amount("1");
            orderSession.setOrder_status("Teslimat");
            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "Sipariş Ekleme Başarılı!");
            hm.put(REnum.RESULT, orderSessionRepository.save(orderSession));
            return hm;
        }else{
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Sipariş Ekleme Başarısız!");
            return hm;
        }

    }

    //REDIS - ORDERS LIST
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> ordersList(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        hm.put(REnum.STATUS, true);
        if (stIndex.equals("0")) {
            hm.put(REnum.RESULT, orderSessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex), Util.pageSize)));
        } else {
            hm.put(REnum.RESULT, orderSessionRepository.findByOrderByIdAsc(PageRequest.of(Integer.parseInt(stIndex) - 1, Util.pageSize)));
        }
        int additional = 0;
        if (orderSessionRepository.count() % 10 != 0) {
            additional = 1;
        }
        hm.put(REnum.COUNTOFPAGE, (orderSessionRepository.count() / Util.pageSize) + additional);
        return hm;
    }

    //ORDERS DELETE
    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> ordersDelete(@RequestBody @PathVariable String stIndex) {
        Map<REnum, Object> hm = new LinkedHashMap<>();
        hm.put(REnum.MESSAGE, "Başarılı");
        orderRepository.deleteById(Integer.valueOf(stIndex));
        orderSessionRepository.deleteById(stIndex);
        hm.put(REnum.STATUS, true);
        return hm;
    }



}
