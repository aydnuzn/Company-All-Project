package com.works.repositories._redis;

import com.works.models._redis.AnnouncementSession;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface AnnouncementSessionRepository extends CrudRepository<AnnouncementSession,String> {
}
