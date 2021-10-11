package com.works.entities.categories;

import com.works.entities.Gallery;
import com.works.entities.listener.BaseEntity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class GalleryCategory extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String gallery_category_title;

    @OneToMany(mappedBy = "galleryCategory", cascade = CascadeType.DETACH)
    private List<Gallery> galleries;

}