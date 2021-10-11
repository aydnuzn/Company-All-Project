package com.works.entities;

import com.works.entities.constant.address.City;
import com.works.entities.constant.address.District;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Address extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String address_detail;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "city_id")
    City city;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "district_id")
    District district;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_id")
    private Customer customer;
}
