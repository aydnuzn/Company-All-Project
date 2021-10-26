package com.works.restcontrollers.adminpanel;

import com.works.business._restcontrollers.adminpanel.OrdersRestControllerBusiness;
import com.works.properties.OrdersInterlayer;
import com.works.utils.REnum;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/rest/admin/orders")
public class OrdersRestController {

    final OrdersRestControllerBusiness business;

    public OrdersRestController(OrdersRestControllerBusiness business) {
        this.business = business;
    }

    @PostMapping("/add")
    public Map<REnum, Object> orderAdd(@Valid @RequestBody OrdersInterlayer ordersInterlayer, BindingResult bindingResult) {
        return business.orderAdd(ordersInterlayer, bindingResult);
    }

    //REDIS - ORDERS LIST
    @GetMapping("/list/{stIndex}")
    public Map<REnum, Object> ordersList(@RequestBody @PathVariable String stIndex) {
        return business.ordersList(stIndex);
    }

    //REDIS - ORDERS DELIVERY LIST
    @GetMapping("/deliveryList/{stIndex}")
    public Map<REnum, Object> ordersDeliveryList(@RequestBody @PathVariable String stIndex) {
        return business.ordersDeliveryList(stIndex);
    }

    //REDIS - ORDERS DELIVERED LIST
    @GetMapping("/deliveredList/{stIndex}")
    public Map<REnum, Object> ordersDeliveredList(@RequestBody @PathVariable String stIndex) {
        return business.ordersDeliveredList(stIndex);
    }

    //ORDER STATUS CHANGE
    @GetMapping("/status/{stIndex}")
    public Map<REnum, Object> orderStatus(@PathVariable String stIndex) {
        return business.orderStatus(stIndex);
    }

    //ORDERS DELETE
    @DeleteMapping("/delete/{stIndex}")
    public Map<REnum, Object> ordersDelete(@RequestBody @PathVariable String stIndex) {
        return business.ordersDelete(stIndex);
    }

}