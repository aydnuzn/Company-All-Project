package com.works.entities;

import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
public class Advertisement extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer adv_id;
    private String adv_title;
    private Integer adv_shown_number;
    @Temporal(TemporalType.DATE)
    private Date adv_date_begin;
    @Temporal(TemporalType.DATE)
    private Date adv_date_end;
    private String adv_image;
    private String adv_width;//genişlik
    private String adv_height;
    private String adv_link;
}