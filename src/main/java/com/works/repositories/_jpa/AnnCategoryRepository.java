package com.works.repositories._jpa;

import com.works.entities.categories.AnnouncementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnnCategoryRepository extends JpaRepository<AnnouncementCategory, Integer> {
    List<AnnouncementCategory> findByCompany_IdEquals(Integer id);

}
