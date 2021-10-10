package com.works.entities.categories;

import com.works.entities.Announcement;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
public class AnnouncementCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String ann_category_title;

    @OneToMany(mappedBy = "announcementCategory", cascade = CascadeType.DETACH)
    private List<Announcement> announcements;

}