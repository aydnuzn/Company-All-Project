package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessioncustomer")
public class CustomerSession {
    @Id
    private String id;

    private String name;
    private String surname;
    private String email;
    private String tel;

    @Indexed
    private String companyname = Util.theCompany == null ? "" : Util.theCompany.getCompany_name();
}
