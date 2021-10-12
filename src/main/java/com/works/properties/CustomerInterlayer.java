package com.works.properties;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

@Data
public class CustomerInterlayer {
    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    private String cu_name;

    @NotNull
    @NotEmpty
    @Length(min = 2, max = 20)
    private String cu_surname;

    @NotNull
    @NotEmpty
    @Email
    private String cu_email;

    @NotNull
    @NotEmpty
    @Pattern(regexp = "^\\d{11}$")
    private String cu_tel;

    @NotNull
    @NotEmpty
    @Length(min = 4, max = 16)
    private String cu_password;

    @NotNull
    @Min(value = 1, message = "En az 1 olabilir")
    private Integer cu_status;

}
//Müşteri Ekleme