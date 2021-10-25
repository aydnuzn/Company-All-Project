package com.works.properties;

import com.works.utils.Util;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
@ApiModel(value = "Adres Ara Katman Modeli", description = "Adres ekleme için kullanılır.")
public class AddressInterlayer {
    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "İl", dataType = Util.integer, required = true, example="34")
    private Integer city_id;

    @NotNull
    @Min(value = 1)
    @ApiModelProperty(value = "İlçe", dataType = Util.integer, required = true, example="10")
    private Integer district_id;

    @NotNull
    @NotEmpty
    @ApiModelProperty(value = "Adres Detayı", dataType = Util.string, required = true, example="Bostancı")
    @Length(max = 500)
    private String address_detail;
}
