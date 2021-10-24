package com.works.entities.images;

import com.works.entities.Gallery;
import com.works.entities.listener.BaseEntity;
import com.works.entities.listener.BaseEntityNotCompany;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@Data
public class GalleryImage extends BaseEntityNotCompany<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    @Length(min = 3, max = 20)
    private String gallery_image_title;
    private String gallery_image_url;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "gallery_id")
    private Gallery gallery;
}
