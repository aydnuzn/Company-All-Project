package com.works.entities;

import com.works.entities.listener.BaseEntityNotCompany;
import com.works.entities.security.User;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class LikeManagement extends BaseEntityNotCompany<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "customer_id")
    private User customer;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id")
    private Product product;

    private Integer score;

}
