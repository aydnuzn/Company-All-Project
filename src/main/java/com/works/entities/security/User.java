package com.works.entities.security;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.works.entities.Address;
import com.works.entities.listener.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
public class User extends BaseEntity<String> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 20)
    private String name;

    @Column(length = 20)
    private String surname;

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true, length = 11)
    private String tel;

    private Integer cu_status;

    private boolean enabled;
    private boolean tokenExpired;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.DETACH)
    private List<Address> addresses;

    @JsonManagedReference
    @ManyToMany
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "ro_id"))
    private List<Role> roles;
}
