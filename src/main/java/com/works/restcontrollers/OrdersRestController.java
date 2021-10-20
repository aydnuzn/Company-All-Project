package com.works.restcontrollers;

import com.works.entities.Orders;
import com.works.entities.Product;
import com.works.entities.security.User;
import com.works.models._redis.OrderSession;
import com.works.repositories._jpa.OrderRepository;
import com.works.repositories._jpa.ProductRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.OrderSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/rest/admin/orders")
public class OrdersRestController {

    final OrderRepository orderRepository;
    final OrderSessionRepository orderSessionRepository;
    final ProductRepository productRepository;
    final UserRepository userRepository;

    public OrdersRestController(OrderRepository orderRepository, OrderSessionRepository orderSessionRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderSessionRepository = orderSessionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @PostMapping("/add")
    public Map<REnum, Object> orderAdd(@Valid Orders orders){

        Map<REnum, Object> hm = new LinkedHashMap<>();
        OrderSession orderSession = new OrderSession();


            //Product IDsi çekme
            Integer productId = 0;
            try {
                productId = Integer.valueOf(orders.getProduct().getId());
            } catch (Exception e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Ürün numarası doğal sayı olmalıdır!");
                hm.put(REnum.ERROR, e);
                return hm;
            }
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (optionalProduct.isPresent()) {
                orders.setProduct(optionalProduct.get());
                orderSession.setProduct_id(String.valueOf(optionalProduct.get().getId()));
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Bu numaraya sahip bir ürün veritabanında bulunamadı!");
                return hm;
            }
            //Customer IDsi çekme
            Integer customerId = 0;
            try {
                productId = Integer.valueOf(orders.getUser().getId());
            } catch (Exception e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Müşteri numarası doğal sayı olmalıdır!");
                hm.put(REnum.ERROR, e);
                return hm;
            }
            Optional<User> optionalUser = userRepository.findById(customerId);
            if (optionalUser.isPresent()) {
                orders.setUser(optionalUser.get());
                orderSession.setUser_id(String.valueOf(optionalUser.get().getId()));
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Bu numaraya sahip bir müşteri veritabanında bulunamadı!");
                return hm;
            }
            Integer orders_amount = 0;
            try {
                orders_amount = Integer.valueOf(orders_amount);
            } catch (NumberFormatException e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Ürün adeti doğal sayılar ile yapılmalıdır!");
                hm.put(REnum.ERROR, e);
                return hm;
            }

            //SCORE
            orderSession.setOrder_amount(String.valueOf(orders_amount));

            orderSession.setCustomer_address(String.valueOf(userRepository.findById(customerId).get().getAddresses()));


            boolean isValid = false;



           orderSession.setId(String.valueOf(orders.getId()));


            if (isValid) {
                hm.put(REnum.STATUS, true);
                hm.put(REnum.MESSAGE, "Başarılı");
                hm.put(REnum.RESULT, orderSessionRepository.save(orderSession));
                return hm;
            } else {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Sipariş verebilmek  için Müşteri olmanız gerekir!");
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
