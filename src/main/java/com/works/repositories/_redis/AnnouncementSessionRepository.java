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

    @Query("select s from sessionannouncement s order by s.id")
    List<AnnouncementSession> findByOrderByIdAsc(Pageable pageable);

    List<AnnouncementSession> findByCompanynameEquals(String companyname, Pageable pageable);



    //@Query("select s from sessionannouncement s where s.companyname = ?1")
    //List<AnnouncementSession> findByAnnouncementSession(String companyname, Pageable pageable);

    //@Query(value = "select s from sessionannouncement s where s.companyname = ?1", nativeQuery = true)
    //List<AnnouncementSession> findByCompanyNameEquals(String companyname, Pageable pageable);


}
