package com.works.entities;

import com.works.entities.images.GalleryImage;
import com.works.entities.listener.BaseEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Entity
public class Gallery extends BaseEntity<String> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotNull
    @NotEmpty
    @Column(length = 16)
    @Length(min = 3, max = 16)
    private String gallery_title;

    @NotNull
    @NotEmpty
    @Column(length = 500)
    @Length(max = 500)
    private String gallery_detail;

    @NotNull
    @Min(value = 1,message = "Seçim Yapınız")
    private Integer gallery_status;

    @OneToMany(mappedBy = "gallery", cascade = CascadeType.DETACH)
    private List<GalleryImage> galleryImages;
}