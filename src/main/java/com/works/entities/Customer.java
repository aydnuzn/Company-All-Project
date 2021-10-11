package com.works.entities;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class Customer extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String cu_name;
    private String cu_surname;

    @Column(unique = true)
    private String cu_mail;

    private String cu_tel;
    private Integer cu_status;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    private List<Address> addresses;
}
