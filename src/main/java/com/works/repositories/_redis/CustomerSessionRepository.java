package com.works.repositories._redis;

import com.works.models._redis.ContentSession;
import com.works.models._redis.CustomerSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories

public interface CustomerSessionRepository extends CrudRepository<CustomerSession, String> {
    @Query("select s from sessioncustomer s order by s.id")
    List<CustomerSession> findByOrderByIdAsc(Pageable pageable);
}
