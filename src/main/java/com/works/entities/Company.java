package com.works.entities;

import com.works.entities.listener.BaseEntityCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Company extends BaseEntityCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String company_name;
    private String company_address;
    private Integer company_sector;
    private String company_tel;
    private String company_logo;
    private String company_lat;
    private String company_lng;

}