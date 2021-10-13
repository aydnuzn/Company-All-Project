package com.works.models._redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@RedisHash("sessioncontent")
public class ContentSession {
    @Id
    private String id;
    private String content_title;
    private String content_brief_description;
    private String content_detailed_description;
    private String content_status; //bu String type olarak değişebilir
}
