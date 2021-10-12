package com.works.entities;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class Customer extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @NotNull
    private String cu_name;

    @NotNull
    @NotEmpty
    private String cu_surname;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String cu_mail;

    @NotNull
    @NotEmpty
    @Column(unique = true)
    private String cu_tel;

    @NotNull
    @Min(value = 1, message = "En az 1 olabilir")
    private Integer cu_status;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    private List<Address> addresses;
}
