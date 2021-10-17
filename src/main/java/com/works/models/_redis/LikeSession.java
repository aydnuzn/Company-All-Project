package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;
@Data
@RedisHash("sessionlike")
public class LikeSession {
    @Id
    private String id;

    private String score;
    private String product;
    private String customer;
    private String date;


    @Indexed
    private String companyname = Util.theCompany == null ? "":Util.theCompany.getCompany_name() ;
}
