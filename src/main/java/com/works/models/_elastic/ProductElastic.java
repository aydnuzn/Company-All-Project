package com.works.models._elastic;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Data
@Document(indexName = "productelastic")
public class ProductElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String pr_name;

    @Field(type = FieldType.Text)
    private String pr_brief_description;

    @Field(type = FieldType.Text)
    private String pr_price;

    @Field(type = FieldType.Text)
    private String pr_type;

    @Field(type = FieldType.Text)
    private String pr_image;

    @Field(type = FieldType.Text)
    private String companyname = Util.theCompany.getCompany_name();
}
