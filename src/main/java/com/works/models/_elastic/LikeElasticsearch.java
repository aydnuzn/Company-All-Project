package com.works.models._elastic;

import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.Id;

@Document(indexName = "likeelasticsearch")
public class LikeElasticsearch {
    @Id
    private String id;
    private String score;
    private String customer;
    private String product;
}
