package com.works.models._redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@RedisHash("sessionannouncement")
public class AnnouncementSession {

    @Id
    private String id;
    private String ann_title;
    private String ann_brief_description;
    private String ann_description;
    private String ann_type;

}
