package com.works.controllers.adminpanel;

import com.works.business._controllers.adminpanel.OrdersControllerBusiness;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/orders")
public class OrdersController {

    final OrdersControllerBusiness business;

    public OrdersController(OrdersControllerBusiness business) {
        this.business = business;
    }

    @GetMapping("/list")
    public String ordersList() {
        return business.ordersList();
    }

    @GetMapping("/deliverylist")
    public String ordersDelivery() {
        return business.ordersDelivery();
    }

    @GetMapping("/deliveredlist")
    public String ordersDelivered() {
        return business.ordersDelivered();
    }
}
