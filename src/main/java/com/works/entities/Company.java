package com.works.entities;

import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class Company extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20, unique = true)
    private String company_name;

    @Column(length = 500)
    private String company_address;

    private Integer company_sector;
    @Column(length = 11,unique = true)
    private String company_tel;
    private String company_logo;
    private String company_lat;
    private String company_lng;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "city_id")
    City city;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "district_id")
    District district;

}