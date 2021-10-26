package com.works.repositories._redis;

import com.works.models._redis.AnnouncementSession;
import com.works.models._redis.ProductSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface ProductSessionRepository extends CrudRepository<ProductSession,String> {

    List<ProductSession> findByCompanynameEquals(String companyname, Pageable pageable);
    List<ProductSession> findByCompanynameEquals(String companyname);

}
