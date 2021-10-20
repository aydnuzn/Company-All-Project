package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionaddress")
public class AddressSession {
    @Id
    private String id;
    private String address_detail;
    private String city_name;
    private String district_name;
    @Indexed
    private String customerid;
}