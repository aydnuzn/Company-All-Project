package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionproductcategory")
public class ProductCategorySession {
    @Id
    private String id;
    private String pr_title;
    private String parent_id;

    @Indexed
    private String companyname = Util.theCompany.getCompany_name();
}
