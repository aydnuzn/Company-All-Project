package com.works.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class RegisterInterlayer {

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    private String company_name;

    @NotNull
    @Min(value = 1)
    private Integer company_city;

    @NotNull
    @Min(value = 1)
    private Integer company_district;

    @NotNull
    @NotEmpty
    @Length(max = 500)
    private String company_address;

    @NotNull
    @Min(value = 1)
    private Integer company_sector;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String company_tel;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    private String admin_name;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    private String admin_surname;

    @NotNull
    @NotEmpty
    @Email
    private String admin_email;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String admin_tel;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 16)
    private String admin_password1;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 16)
    private String admin_password2;
}
//Firma - Yönetici Ekleme