package com.works.models._elastic;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;


@Data
@Document(indexName = "advertisementelasticsearch")
public class AdvertisementElasticsearch {
    @Id
    private String id;
    @Field(type = FieldType.Text)
    private String adv_title;
    @Field(type = FieldType.Text)
    private String adv_shown_number;
    @Field(type = FieldType.Text)
    private String adv_date_begin;
    @Field(type = FieldType.Text)
    private String adv_date_end;
    @Field(type = FieldType.Text)
    private String adv_image;
    @Field(type = FieldType.Text)
    private String adv_width;//genişlik
    @Field(type = FieldType.Text)
    private String adv_height;
    @Field(type = FieldType.Text)
    private String adv_link;
    @Field(type = FieldType.Text)
    private String companyname = Util.theCompany.getCompany_name();
}
