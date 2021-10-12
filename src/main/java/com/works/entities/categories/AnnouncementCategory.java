package com.works.entities.categories;

import com.works.entities.Announcement;
import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class AnnouncementCategory extends BaseEntity<String> {
    //Duyuru
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true)
    private String ann_category_title;

    @OneToMany(mappedBy = "announcementCategory", cascade = CascadeType.DETACH)
    private List<Announcement> announcements;

}