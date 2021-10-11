package com.works.entities;

import com.works.entities.listener.BaseEntity;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@Entity
public class Advertisement extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @NotNull
    @NotEmpty
    private String adv_title;
    private Integer adv_shown_number;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date adv_date_begin;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date adv_date_end;
    private String adv_image;
    private String adv_width;//genişlik
    private String adv_height;
    private String adv_link;
}