package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionproduct")
public class ProductSession {
    @Id
    private String id;
    private String pr_name;
    private String pr_brief_description;
    private String pr_price;
    private String pr_type;
    private String pr_image;

    @Indexed
    private String companyname = Util.theCompany.getCompany_name();
}
