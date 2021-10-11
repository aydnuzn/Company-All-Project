package com.works.entities;

import com.works.entities.categories.GalleryCategory;
import com.works.entities.images.GalleryImage;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Gallery extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String gallery_title;
    private String gallery_detail;
    private Integer gallery_status;
    private String gallery_catalog_image_url;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "gallery_category_id")
    private GalleryCategory galleryCategory;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.DETACH)
    private List<GalleryImage> galleryImages;
}