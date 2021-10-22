package com.works.repositories._elastic;

import com.works.models._elastic.ProductElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductElasticRepository extends ElasticsearchRepository<ProductElastic,String> {

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"pr_name\":\"?0\"}}]}}")
    Page<ProductElastic> findByPr_name(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"pr_name\":\"?0\"}}]}}")
    List<ProductElastic> findByPr_name(String key);

}
