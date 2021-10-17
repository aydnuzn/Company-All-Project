package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionsurvey")
public class SurveySession {
    @Id
    private String id;
    private String survey_title;
    @Indexed
    private String companyname = Util.theCompany.getCompany_name();
}