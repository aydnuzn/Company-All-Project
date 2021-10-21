package com.works.properties;

import lombok.Data;

@Data
public class OrdersInterlayer {

    private String user_id;
    private String product_id;
    private String order_amount;
    private String customer_address;
}
