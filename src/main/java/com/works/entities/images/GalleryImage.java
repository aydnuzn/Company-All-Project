package com.works.entities.images;

import com.works.entities.Gallery;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class GalleryImage extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String gallery_image_title;
    private String gallery_image_url;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;
}
