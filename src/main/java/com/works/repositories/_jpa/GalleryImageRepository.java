package com.works.repositories._jpa;

import com.works.entities.images.GalleryImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GalleryImageRepository extends JpaRepository<GalleryImage, Integer> {
}
