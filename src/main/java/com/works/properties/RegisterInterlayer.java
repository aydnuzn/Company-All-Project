package com.works.properties;

import lombok.Data;

@Data
public class RegisterInterlayer {
    private String company_name;
    private String company_address;
    private Integer company_sector;
    private String company_tel;
    private String admin_name;
    private String admin_surname;
    private String admin_email;
    private String admin_tel;
    private String admin_password1;
    private String admin_password2;
}