package com.works.models._redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@Data
@RedisHash("sessionsurvey")
public class SurveySession {
    @Id
    private String id;
    private String survey_title;
}