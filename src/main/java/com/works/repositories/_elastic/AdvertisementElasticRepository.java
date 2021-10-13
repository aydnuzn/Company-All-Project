package com.works.repositories._elastic;

import com.works.models._elastic.AdvertisementElasticsearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AdvertisementElasticRepository extends ElasticsearchRepository<AdvertisementElasticsearch, String> {
    @Query(value = "{\"bool\":{\"must\":{\"match\":{\"adv_title\":\"?0\"}}}}")
    Page<AdvertisementElasticsearch> findByAdv_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":{\"match\":{\"adv_title\":\"?0\"}}}}")
    List<AdvertisementElasticsearch> findByAdv_title(String key);
}
