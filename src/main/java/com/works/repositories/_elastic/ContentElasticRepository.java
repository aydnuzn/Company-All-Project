package com.works.repositories._elastic;

import com.works.models._elastic.ContentElasticsearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.annotations.Query;

import java.util.List;

public interface ContentElasticRepository extends ElasticsearchRepository<ContentElasticsearch, String> {

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"content_title\":\"?0\"}}]}}")
    Page<ContentElasticsearch> findByContent_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"content_title\":\"?0\"}}]}}")
    List<ContentElasticsearch> findByContent_title(String key);




}
