package com.works.repositories._jpa;

import com.works.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Integer> {
    @Query("select c from Company c where c.company_name = ?1")
    Optional<Company> findByCompany_nameEquals(String company_name);



}
