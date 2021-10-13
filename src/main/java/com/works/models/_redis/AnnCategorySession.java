package com.works.models._redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@RedisHash("sessionanncategory")
public class AnnCategorySession {
    @Id
    private String id;
    private String ann_category_title;
}