package com.works.repositories._redis;

import com.works.models._redis.AdvertisementSession;
import com.works.models._redis.ContentSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface AdvertisementSessionRepository extends CrudRepository<AdvertisementSession, String> {

    List<AdvertisementSession> findByCompanynameEquals(String companyname, Pageable pageable);

    List<AdvertisementSession> findByCompanynameEquals(String companyname);

}
