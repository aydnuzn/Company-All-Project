package com.works.entities;

import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Company extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String company_name;
    private String company_address;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "city_id")
    City city;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "district_id")
    District district;

    private Integer company_sector;
    private String company_tel;
    private String company_logo;
    private String company_lat;
    private String company_lng;

}