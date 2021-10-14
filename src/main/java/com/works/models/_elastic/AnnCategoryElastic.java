package com.works.models._elastic;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "anncategoryelastic")
@Data
public class AnnCategoryElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String ann_category_title;

}
