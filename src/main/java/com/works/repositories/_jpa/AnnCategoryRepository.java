package com.works.repositories._jpa;

import com.works.entities.categories.AnnouncementCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnCategoryRepository extends JpaRepository<AnnouncementCategory, Integer> {
}
