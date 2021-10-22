package com.works.restcontrollers;

import com.works.entities.Orders;
import com.works.entities.Product;
import com.works.entities.security.User;
import com.works.models._redis.OrderSession;
import com.works.properties.OrdersInterlayer;
import com.works.repositories._jpa.AddressRepository;
import com.works.repositories._jpa.OrderRepository;
import com.works.repositories._jpa.ProductRepository;
import com.works.repositories._jpa.UserRepository;
import com.works.repositories._redis.OrderSessionRepository;
import com.works.utils.REnum;
import com.works.utils.Util;
import org.springframework.data.domain.PageRequest;
import org.springframework.validation.BindingResult;
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
    final AddressRepository addressRepository;

    public OrdersRestController(OrderRepository orderRepository, OrderSessionRepository orderSessionRepository, ProductRepository productRepository, UserRepository userRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.orderSessionRepository = orderSessionRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @PostMapping("/add")
    public Map<REnum, Object> orderAdd(@Valid @RequestBody OrdersInterlayer ordersInterlayer, Orders orders, BindingResult bindingResult){
        Map<REnum, Object> hm = new LinkedHashMap<>();
        OrderSession orderSession = new OrderSession();
        System.out.println(" ORderrrrrrr: " + ordersInterlayer) ;
        if(!bindingResult.hasErrors()){

            //PRODUCT
            Integer productId = 0;
            try {
                productId = Integer.valueOf(ordersInterlayer.getProduct_id());
            } catch (Exception e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Ürün numarası doğal sayı olmalıdır!");
                hm.put(REnum.ERROR, e);
                return hm;
            }
            Optional<Product> optionalProduct = productRepository.findById(productId);
            if (!optionalProduct.isPresent()) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Bu numaraya sahip bir ürün veritabanında bulunamadı!");
                return hm;
            }
            orders.setProduct(optionalProduct.get());
            orderSession.setProduct_id(String.valueOf(orders.getProduct().getId()));
            //CUSTOMER
            Integer customerId = 0;
            try {
                customerId = Integer.valueOf(ordersInterlayer.getUser_id());
            } catch (Exception e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Müşteri numarası doğal sayı olmalıdır!");
                hm.put(REnum.ERROR, e);
                return hm;
            }
            Optional<User> optionalUser = userRepository.findById(customerId);
            if (!optionalUser.isPresent()) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Bu numaraya sahip bir müşteri veritabanında bulunamadı!");
                return hm;
            }
            orders.setUser(optionalUser.get());
            orderSession.setUser_id(String.valueOf(orders.getUser().getId()));


            Integer orders_amount = 0;
            try {
                orders_amount = Integer.valueOf(ordersInterlayer.getOrder_amount());
            } catch (NumberFormatException e) {
                hm.put(REnum.STATUS, false);
                hm.put(REnum.MESSAGE, "Ürün adeti doğal sayılar ile yapılmalıdır!");
                hm.put(REnum.ERROR, e);
                return hm;
            }



            //ADET
            ordersInterlayer.setOrder_amount(String.valueOf(orders_amount));
            orderSession.setOrder_amount(String.valueOf(orders_amount));
            orders.setOrder_amount(orders_amount);


            //ADRES

            orderSession.setCustomer_address(ordersInterlayer.getCustomer_address());
            orders.setAddress(addressRepository.findById(Integer.valueOf(ordersInterlayer.getCustomer_address())).get());
            //orderSession.setCustomer_address(String.valueOf(.findById(customerId).get().getAddresses().get(Integer.parseInt(ordersInterlayer.getCustomer_address()))));
            //orders.setAddress(userRepository.findById(customerId).get().getAddresses().get(Integer.parseInt(ordersInterlayer.getCustomer_address())));

            orders.setOrder_status(false);
            orderSession.setOrder_status(String.valueOf(false));

            orders = orderRepository.save(orders); //normal databasee kaydetme

            //ID
            orderSession.setId(String.valueOf(orders.getId()));

            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "Başarılı");
            hm.put(REnum.RESULT, orderSessionRepository.save(orderSession)); //redis kaydetme
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

    //ORDER STATUS CHANGE
    @GetMapping("/status/{stIndex}")
    public Map<REnum,Object> orderStatus(@PathVariable String stIndex){
        Map<REnum,Object> hm = new LinkedHashMap<>();
        Optional<OrderSession> optionalOrderSession = orderSessionRepository.findById(stIndex);
        if(optionalOrderSession.get().getOrder_status().equals("0")){
            if(optionalOrderSession.isPresent()){
                OrderSession orderSession =  optionalOrderSession.get();
                orderSession.setOrder_status("1");
                orderSessionRepository.deleteById(stIndex);
                orderSessionRepository.save(orderSession);

                Orders orders = orderRepository.findById(Integer.valueOf(stIndex)).get();
                orders.setOrder_status(true);
                orderRepository.saveAndFlush(orders);

            }else{
                System.err.println("Böyle bir sipariş yok!");
            }
        }else{
            if(optionalOrderSession.isPresent()){
                OrderSession orderSession =  optionalOrderSession.get();
                orderSession.setOrder_status("0");
                orderSessionRepository.deleteById(stIndex);
                orderSessionRepository.save(orderSession);

                Orders orders = orderRepository.findById(Integer.valueOf(stIndex)).get();
                orders.setOrder_status(false);
                orderRepository.saveAndFlush(orders);

            }else{
                System.err.println("Böyle bir sipariş yok!");
            }
        }

        try {
            int id = Integer.parseInt(stIndex);
            hm.put(REnum.STATUS, true);
            hm.put(REnum.MESSAGE, "Başarılı!");

            return hm;
        } catch (NumberFormatException e) {
            hm.put(REnum.STATUS, false);
            hm.put(REnum.MESSAGE, "Sipariş durumu 0 ya da 1 olmalıdır!");
            hm.put(REnum.ERROR, e);
            return hm;
        }

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
