package com.works.repositories._jpa;

import com.works.entities.Gallery;
import com.works.entities.projections.GalleryInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GalleryRepository extends JpaRepository<Gallery, Integer> {
    List<Gallery> findByCompany_IdEquals(Integer id);

    @Query(value = "SELECT g.id as gallery_id, g.gallery_title, gi.gallery_image_url FROM `gallery_image` as gi\n" +
            "RIGHT JOIN gallery as g ON g.id = gi.gallery_id\n" +
            "Where g.company_id = ?1 GROUP BY gi.gallery_id;",nativeQuery = true)
    List<GalleryInfo> getGalleryInfo(Integer company);
}
