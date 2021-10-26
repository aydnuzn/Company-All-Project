package com.works.properties;

import com.works.entities.categories.ProductCategory;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProductInterlayer {
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 50, message = "ürün ismi an az 2, en fazla 50 karakter olabilir")
    private String pr_name;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 100, message = "ürün kısa açıklaması an az 4, en fazla 100 karakter olabilir")
    private String pr_brief_description;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 500, message = "ürün Açıklaması an az 4, en fazla 500 karakter olabilir")
    private String pr_description;

    @NotNull
    @NotEmpty
    private String pr_price;

    @NotNull
    @Min(value=1, message = "En az 1 olabilir")
    private Integer pr_type;

    @NotNull
    @Min(value=1, message = "En az 1 olabilir")
    private Integer pr_campaign;

    private String pr_campaign_name;
    private String pr_campaign_description;

    @NotNull
    @NotEmpty
    private String pr_address;

    @NotNull
    private Integer pr_latitude;

    @NotNull
    private Integer pr_longitude;

    @NotNull
    private List<Integer> pr_categories;
}
