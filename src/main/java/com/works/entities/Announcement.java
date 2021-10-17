package com.works.entities;

import com.works.entities.categories.AnnouncementCategory;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
public class Announcement extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ann_title;

    @Column(length = 100)
    private String ann_brief_description;

    @Column(length = 1000)
    private String ann_description;
    private Integer ann_type;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "announcement_category_id")
    private AnnouncementCategory announcementCategory;
}