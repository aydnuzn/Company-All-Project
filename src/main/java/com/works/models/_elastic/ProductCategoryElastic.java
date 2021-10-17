package com.works.models._elastic;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "productcategoryelastic")
@Data
public class ProductCategoryElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String pr_title;

    @Field(type = FieldType.Text)
    private String parent_id;

    @Field(type = FieldType.Text)
    private String companyname = Util.theCompany.getCompany_name();
}
