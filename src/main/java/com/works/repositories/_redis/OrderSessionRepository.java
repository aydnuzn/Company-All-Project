package com.works.repositories._redis;

import com.works.models._redis.LikeSession;
import com.works.models._redis.OrderSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
@EnableRedisRepositories
public interface OrderSessionRepository extends CrudRepository<OrderSession,String> {

    @Query("select s from sessionorder s order by s.id")
    List<OrderSession> findByOrderByIdAsc(Pageable pageable);
}
