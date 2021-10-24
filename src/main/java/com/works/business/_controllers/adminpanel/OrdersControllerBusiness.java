package com.works.business._controllers.adminpanel;

import org.springframework.stereotype.Service;

@Service
public class OrdersControllerBusiness {

    final String rvalue = "adminpanel/orders/";

    public String ordersList() {
        return rvalue + "orderslist";
    }

    public String ordersDelivery() {
        return rvalue + "deliverylist";
    }

    public String ordersDelivered() {
        return rvalue + "deliveredlist";
    }
}
