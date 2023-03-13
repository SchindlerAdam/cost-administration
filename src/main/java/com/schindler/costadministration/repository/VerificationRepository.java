package com.schindler.costadministration.repository;

import com.schindler.costadministration.entities.VerificationCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRepository extends JpaRepository<VerificationCode, Long> {

    @Query("SELECT vc FROM VerificationCode vc WHERE vc.code = :code")
    VerificationCode findVerificationCodeByCode(String code);
}
