package com.works.repositories._elastic;

import com.works.models._elastic.AnnCategoryElastic;
import com.works.models._elastic.ProductCategoryElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface ProductCategoryElasticRepository extends ElasticsearchRepository<ProductCategoryElastic, String> {
    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"pr_title\":\"?0\"}}]}}")
    Page<AnnCategoryElastic> findByPr_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"pr_title\":\"?0\"}}]}}")
    List<AnnCategoryElastic> findByPr_title(String key);
}
