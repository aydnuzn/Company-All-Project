package com.works.properties;

import com.works.utils.Util;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
@ApiModel(value = "Müşteri Ara Katman Modeli", description = "Müşteri ekleme için kullanılır.")
public class CustomerInterlayer {
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    @ApiModelProperty(value = "Müşteri Adı", dataType = Util.string, required = true, example="Ali")
    private String cu_name;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    @ApiModelProperty(value = "Müşteri Soyadı", dataType = Util.string, required = true, example="Bilmem")
    private String cu_surname;

    @NotNull
    @NotEmpty
    @Email
    @ApiModelProperty(value = "Müşteri E-Mail", dataType = Util.string, required = true, example="ali@mail.com")
    private String cu_email;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    @ApiModelProperty(value = "Müşteri Telefon", dataType = Util.string, required = true, example="05322717631")
    private String cu_tel;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 16)
    @ApiModelProperty(value = "Müşteri Şifre", dataType = Util.string, required = true, example="123456")
    private String cu_password;

    @NotNull
    @Min(value = 1, message = "En az 1 olabilir")
    @ApiModelProperty(value = "Müşteri Durum", dataType = Util.integer, required = true, example="1")
    private Integer cu_status;

}
