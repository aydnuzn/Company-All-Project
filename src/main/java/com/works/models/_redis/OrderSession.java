package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionorder")
public class OrderSession {
    @Id
    private String id;
    private String user_id;
    private String product_id;
    private String order_amount;
    private String customer_address;
    private String order_status;

    @Indexed
    private String companyname = Util.theCompany.getCompany_name();
}
