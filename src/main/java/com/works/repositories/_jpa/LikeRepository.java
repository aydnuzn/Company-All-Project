package com.works.repositories._jpa;

import com.works.entities.LikeManagement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<LikeManagement, Integer> {
}
