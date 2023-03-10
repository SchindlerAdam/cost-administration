package com.schindler.costadministration.repository;

import com.schindler.costadministration.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.tokenContent = :token AND t.isExpiredByLogout = FALSE")
    Optional<Token> findTokenByToken(String token);

    @Query("SELECT t FROM Token t WHERE t.user.userId = :userId AND t.isExpiredByLogout = FALSE")
    List<Token> findAllTokenByUserId(Long userId);
}
