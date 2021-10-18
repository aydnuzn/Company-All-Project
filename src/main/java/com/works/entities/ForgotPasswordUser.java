package com.works.entities;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Data
public class ForgotPasswordUser {
    @Id
    private String forgot_id;
    private String us_mail;
    private Boolean status;
}