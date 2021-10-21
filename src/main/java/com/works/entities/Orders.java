package com.works.entities;

import com.works.entities.listener.BaseEntity;
import com.works.entities.security.User;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
public class Orders extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "product_id")
    private Product product;


    private Integer order_amount;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "address_id")
    private Address address;

    private Boolean order_status;


}
