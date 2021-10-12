package com.works.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class RegisterInterlayer {
    private String company_name;
    private String company_address;
    private Integer company_sector;
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