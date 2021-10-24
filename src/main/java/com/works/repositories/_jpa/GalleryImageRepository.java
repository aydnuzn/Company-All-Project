package com.works.repositories._jpa;

import com.works.entities.images.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Integer> {
    List<GalleryImage> findByGallery_IdEquals(Integer id);

}
