package com.schindler.costadministration.service;

import com.schindler.costadministration.entities.Token;
import com.schindler.costadministration.model.TokenModel;
import com.schindler.costadministration.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class TokenService {

    private final TokenRepository tokenRepository;
    @Autowired
    public TokenService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void saveToken(TokenModel tokenModel) {
       this.tokenRepository.save(new Token(tokenModel));
    }
}
