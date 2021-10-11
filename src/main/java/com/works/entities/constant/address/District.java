package com.works.entities.constant.address;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class District {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50)
    private String district_name;

    private Integer city_id;
}