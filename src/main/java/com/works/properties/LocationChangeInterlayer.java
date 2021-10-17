package com.works.properties;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class LocationChangeInterlayer {
    @NotNull
    @NotEmpty
    String company_lat;

    @NotNull
    @NotEmpty
    String company_lng;
}
