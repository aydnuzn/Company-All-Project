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
public interface OrderSessionRepository extends CrudRepository<OrderSession, String> {
    List<OrderSession> findByCompanynameEquals(String companyname, Pageable pageable);
    List<OrderSession> findByCompanynameEquals(String companyname);


    List<OrderSession> findByOrderstatusEqualsAndCompanynameEquals(String orderstatus, String companyname, Pageable pageable);

    List<OrderSession> findByOrderstatusEqualsAndCompanynameEquals(String orderstatus, String companyname);

}
