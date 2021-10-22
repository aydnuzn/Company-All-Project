package com.works.repositories._redis;
import com.works.models._redis.OrderSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;
import java.util.List;
@EnableRedisRepositories
@Transactional
public interface OrderSessionRepository extends CrudRepository<OrderSession,String> {

    @Query("select s from sessionorder s order by s.id")
    List<OrderSession> findByOrderByIdAsc(Pageable pageable);

    /*
    @Modifying
    @Query(value = "update sessionorder s set s.order_status = 1 where s.id=?1", nativeQuery = true)
    Object orderStatusChange(int id);
     */



}
