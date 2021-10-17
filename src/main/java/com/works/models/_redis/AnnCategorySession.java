package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionanncategory")
public class AnnCategorySession {
    @Id
    private String id;
    private String ann_category_title;

    @Indexed
    private String companyname = Util.theCompany == null ? "":Util.theCompany.getCompany_name();
}