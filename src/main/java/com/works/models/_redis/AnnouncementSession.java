package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
@RedisHash("sessionannouncement")
public class AnnouncementSession {

    @Id
    private String id;
    private String ann_title;
    private String ann_brief_description;
    private String ann_type;

    @Indexed
    private String companyname = Util.theCompany == null ? "":Util.theCompany.getCompany_name();

}
