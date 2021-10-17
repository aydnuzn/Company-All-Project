package com.works.repositories._elastic;

import com.works.models._elastic.AdvertisementElasticsearch;
import com.works.models._elastic.LikeElasticsearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface LikeElasticRepository extends ElasticsearchRepository<LikeElasticsearch,String> {

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"customer\":\"?0\"}}]}}")
    Page<LikeElasticsearch> findByCustomer(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"customer\":\"?0\"}}]}}")
    List<LikeElasticsearch> findByCustomer(String key);
}
