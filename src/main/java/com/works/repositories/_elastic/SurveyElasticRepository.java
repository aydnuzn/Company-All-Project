package com.works.repositories._elastic;

import com.works.models._elastic.SurveyElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;

import java.util.List;

public interface SurveyElasticRepository extends ElasticsearchRepository<SurveyElastic, String> {
    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"survey_title\":\"?0\"}}]}}")
    Page<SurveyElastic> findBySurvey_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"survey_title\":\"?0\"}}]}}")
    List<SurveyElastic> findBySurvey_title(String key);
}
