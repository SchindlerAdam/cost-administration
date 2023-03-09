package com.schindler.costadministration.repository;

import com.schindler.costadministration.entities.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {
    @Query("SELECT t FROM Token t WHERE t.token = :token")
    Optional<Token> findTokenByToken(String token);
}
