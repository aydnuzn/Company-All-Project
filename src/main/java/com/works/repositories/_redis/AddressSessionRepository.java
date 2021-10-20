package com.works.repositories._redis;

import com.works.models._redis.AddressSession;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AddressSessionRepository extends CrudRepository<AddressSession, String> {
    List<AddressSession> findByCustomerid(String customerid);
}
