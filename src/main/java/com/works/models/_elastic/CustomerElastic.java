package com.works.models._elastic;

import com.works.utils.Util;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import javax.persistence.Id;

@Document(indexName = "customerelastic")
@Data
public class CustomerElastic {
    @Id
    private String id;

    @Field(type = FieldType.Text)
    private String name;

    @Field(type = FieldType.Text)
    private String surname;

    @Field(type = FieldType.Text)
    private String email;

    @Field(type = FieldType.Text)
    private String tel;

    @Field(type = FieldType.Text)
    private Integer cu_status;

    @Field(type = FieldType.Text)
    private String companyname = Util.theCompany.getCompany_name();

}
