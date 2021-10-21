package com.works.entities;

import com.works.entities.listener.BaseEntityNotCompany;
import com.works.entities.security.User;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class MailVerificationUser extends BaseEntityNotCompany<String> {
    @Id
    private String uuid;

    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "id")
    User user;

}