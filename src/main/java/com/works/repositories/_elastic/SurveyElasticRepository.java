package com.works.repositories._elastic;

import com.works.models._elastic.Survey_;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;

import java.util.List;

public interface SurveyElasticRepository extends ElasticsearchRepository<Survey_, String> {
    @Query(value = "{\"bool\":{\"must\":{\"match\":{\"survey_title\":\"?0\"}}}}")
    Page<Survey_> findBySurvey_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":{\"match\":{\"survey_title\":\"?0\"}}}}")
    List<Survey_> findBySurvey_title(String key);
}
