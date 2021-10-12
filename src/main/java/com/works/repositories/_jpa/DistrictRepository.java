package com.works.repositories._jpa;

import com.works.entities.constant.address.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends JpaRepository<District, Integer> {

    @Query("select d from District d where d.city_id = ?1")
    List<District> findByCity_idEquals(Integer city_id);


}
