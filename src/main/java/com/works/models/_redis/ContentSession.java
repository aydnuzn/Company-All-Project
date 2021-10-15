package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessioncontent")
public class ContentSession {
    @Id
    private String id;
    private String content_title;
    private String content_brief_description;
    private String content_detailed_description;
    private String content_status;
    @Indexed
    private String companyname = Util.theCompany == null ? "":Util.theCompany.getCompany_name() ;
}
