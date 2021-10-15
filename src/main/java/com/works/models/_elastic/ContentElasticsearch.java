package com.works.models._elastic;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "contentelasticsearch")
@Data
public class ContentElasticsearch {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String content_title;
    @Field(type = FieldType.Text)
    private String content_brief_description;
    @Field(type = FieldType.Text)
    private String content_detailed_description;
    @Field(type = FieldType.Text)
    private String content_status;
    @Field(type = FieldType.Text)
    private String companyname = Util.theCompany.getCompany_name();
}
