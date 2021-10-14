package com.works.repositories._redis;

import com.works.models._redis.AnnouncementSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface AnnouncementSessionRepository extends CrudRepository<AnnouncementSession,String> {

    @Query("select s from sessionannouncement s order by s.id")
    List<AnnouncementSession> findByOrderByIdAsc(Pageable pageable);
}
