package com.works.repositories._jpa;

import com.works.entities.MailVerificationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailVerificationUserRepository extends JpaRepository<MailVerificationUser, String> {
}
