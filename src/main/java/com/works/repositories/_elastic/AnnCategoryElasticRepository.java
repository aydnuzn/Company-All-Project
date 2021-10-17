package com.works.repositories._elastic;


import com.works.models._elastic.AnnCategoryElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AnnCategoryElasticRepository extends ElasticsearchRepository<AnnCategoryElastic, String> {

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"ann_category_title\":\"?0\"}}]}}")
    Page<AnnCategoryElastic> findByAnn_category_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"ann_category_title\":\"?0\"}}]}}")
    List<AnnCategoryElastic> findByAnn_category_title(String key);

}
