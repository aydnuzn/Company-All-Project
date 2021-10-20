package com.works.repositories._elastic;

import com.works.models._elastic.CustomerElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface CustomerElasticRepository extends ElasticsearchRepository<CustomerElastic, String> {

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"name\":\"?0\"}}]}}")
    Page<CustomerElastic> findByName(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"name\":\"?0\"}}]}}")
    List<CustomerElastic> findByName(String key);


}
