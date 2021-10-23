package com.works.repositories._redis;

import com.works.entities.Announcement;
import com.works.entities.Company;
import com.works.models._redis.AnnouncementSession;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

@EnableRedisRepositories
public interface AnnouncementSessionRepository extends CrudRepository<AnnouncementSession,String> {

    List<AnnouncementSession> findByCompanynameEquals(String companyname, Pageable pageable);
    List<AnnouncementSession> findByCompanynameEquals(String companyname);

}
