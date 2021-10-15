package com.works.entities.categories;

import com.works.entities.Announcement;
import com.works.entities.listener.BaseEntity;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Data
public class AnnouncementCategory extends BaseEntity<String> {
    //Duyuru
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 50, message = "En az 3, en fazla 50 karakter girilebilir.")
    @Column(unique = true)
    private String ann_category_title;

    @OneToMany(mappedBy = "announcementCategory", cascade = CascadeType.DETACH)
    private List<Announcement> announcements;

}