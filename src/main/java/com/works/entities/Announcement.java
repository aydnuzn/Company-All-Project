package com.works.entities;

import com.works.entities.categories.AnnouncementCategory;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Announcement extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ann_title;

    private String ann_brief_description;

    @Column(length = 500)
    private String ann_description;

    private Integer ann_type;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "announcement_category_id")
    private AnnouncementCategory announcementCategory;
}