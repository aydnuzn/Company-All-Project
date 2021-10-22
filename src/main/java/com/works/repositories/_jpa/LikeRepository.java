package com.works.repositories._jpa;

import com.works.entities.LikeManagement;
import com.works.entities.projections.LikeInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import javax.transaction.Transactional;
import java.util.List;
@Transactional
public interface LikeRepository extends JpaRepository<LikeManagement, Integer> {


    @Query(value = "select sum(score) as totalScore, product_id as productID from like_management group by product_id", nativeQuery = true)
    List<LikeInfo> findAllLikes();
}
