package com.works.repositories._elastic;

import com.works.models._elastic.AnnouncementElastic;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

public interface AnnouncementElasticRepository extends ElasticsearchRepository<AnnouncementElastic, String> {
    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"ann_title\":\"?0\"}}]}}")
    Page<AnnouncementElastic> findByAnn_title(String key, Pageable pageable);

    @Query(value = "{\"bool\":{\"must\":[{\"match\":{\"companyname\":\"?0\"}},{\"match\":{\"ann_title\":\"?0\"}}]}}")
    List<AnnouncementElastic> findByAnn_title(String key);
}
