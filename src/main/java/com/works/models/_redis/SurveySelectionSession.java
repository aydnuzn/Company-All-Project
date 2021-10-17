package com.works.models._redis;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Data
@RedisHash("sessionsurveyselection")
public class SurveySelectionSession {
    @Id
    private String id;
    private String survey_selection_title;
    private Integer survey_selection_score;
    @Indexed
    private String surveyid;
    @Indexed
    private String companyname = Util.theCompany.getCompany_name();
}
