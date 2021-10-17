package com.works.models._elastic;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "announcementelastic")
@Data
public class AnnouncementElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String ann_title;

    @Field(type=FieldType.Text)
    private String ann_brief_description;

    @Field(type=FieldType.Text)
    private String ann_type;

    @Field(type = FieldType.Text)
    private String companyname = Util.theCompany.getCompany_name();

}
