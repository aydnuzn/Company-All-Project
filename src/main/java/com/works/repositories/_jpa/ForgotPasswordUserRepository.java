package com.works.repositories._jpa;

import com.works.entities.ForgotPasswordUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ForgotPasswordUserRepository extends JpaRepository<ForgotPasswordUser, String> {
    @Query("select f from ForgotPasswordUser f where upper(f.us_mail) = upper(?1)")
    Optional<ForgotPasswordUser> findByForgotMail(String us_mail);

}
