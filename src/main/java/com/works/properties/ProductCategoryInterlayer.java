package com.works.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductCategoryInterlayer {

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 40, message = "Kategori ismi an az 2, en fazla 40 karakter olabilir")
    private String pr_title;

    @NotNull
    private Integer pr_category;

}
