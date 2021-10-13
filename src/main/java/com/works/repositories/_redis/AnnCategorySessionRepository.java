package com.works.repositories._redis;

import com.works.models._redis.AnnCategorySession;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

@EnableRedisRepositories
public interface AnnCategorySessionRepository extends CrudRepository<AnnCategorySession, String> {
}
