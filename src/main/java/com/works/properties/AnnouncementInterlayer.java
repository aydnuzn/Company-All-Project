package com.works.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnouncementInterlayer {

    @NotNull
    @NotEmpty
    private String ann_title;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 100, message = "Duyuru kısa açıklama an az 3, en fazla 100 karakter olabilir")
    private String ann_brief_description;

    @NotNull
    @NotEmpty
    private String ann_description;

    @NotNull
    @Min(value = 1, message = "En az 1 olabilir")
    private Integer ann_type;

    @NotNull
    @Min(value=1, message = "En az 1 olabilir")
    private Integer ann_category;
}
