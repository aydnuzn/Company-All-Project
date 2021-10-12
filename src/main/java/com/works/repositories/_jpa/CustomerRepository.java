package com.works.repositories._jpa;

import com.works.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

    @Query("select c from Customer c where c.cu_mail = ?1")
    Optional<Customer> findByCu_mailEquals(String cu_mail);
}
