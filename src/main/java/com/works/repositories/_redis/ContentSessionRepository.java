package com.works.repositories._redis;

import com.works.models._redis.ContentSession;
import com.works.models._redis.SurveySession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface ContentSessionRepository extends CrudRepository<ContentSession, String> {

    @Query("select s from sessioncontent s order by s.id")
    List<ContentSession> findByOrderByIdAsc(Pageable pageable);
}
