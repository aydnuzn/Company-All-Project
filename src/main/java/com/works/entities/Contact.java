package com.works.entities;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotEmpty
    @NotNull
    @Length(max = 50)
    @Column(length = 50)
    private String contact_name;

    @NotEmpty
    @NotNull
    @Length(max = 50)
    @Column(length = 50)
    private String contact_mail;

    @NotEmpty
    @NotNull
    @Length(max = 1000)
    @Column(length = 1000)
    private String contact_message;
    
    private Date date;
}
