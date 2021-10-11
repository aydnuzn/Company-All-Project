package com.works.entities.constant.address;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class City {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String city_name;
}