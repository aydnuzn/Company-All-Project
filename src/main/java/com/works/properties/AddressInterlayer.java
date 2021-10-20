package com.works.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data

public class AddressInterlayer {
    @NotNull
    @Min(value = 1)
    private Integer city_id;

    @NotNull
    @Min(value = 1)
    private Integer district_id;

    @NotNull
    @NotEmpty
    @Length(max = 500)
    private String address_detail;
}
