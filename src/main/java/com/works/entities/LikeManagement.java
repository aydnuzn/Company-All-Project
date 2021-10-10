package com.works.entities;

import com.works.entities.Customer;
import com.works.entities.Product;
import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Like extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer score;




}
