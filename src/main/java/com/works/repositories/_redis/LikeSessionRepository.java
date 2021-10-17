package com.works.repositories._redis;

import com.works.models._redis.LikeSession;
import com.works.models._redis.SurveySession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LikeSessionRepository extends CrudRepository<LikeSession,String> {

    @Query("select s from sessionlike s order by s.id")
    List<LikeSession> findByOrderByIdAsc(Pageable pageable);
}
