package com.works.repositories._redis;

import com.works.models._redis.ProductCategorySession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface ProductCategorySessionRepository extends CrudRepository<ProductCategorySession, String> {

    List<ProductCategorySession> findByCompanynameEquals(String companyname, Pageable pageable);
    List<ProductCategorySession> findByCompanynameEquals(String companyname);




}
