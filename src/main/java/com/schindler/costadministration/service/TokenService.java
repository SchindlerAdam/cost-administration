package com.schindler.costadministration.service;

import com.schindler.costadministration.entities.Token;
import com.schindler.costadministration.model.TokenModel;
import com.schindler.costadministration.repository.TokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public void expireTokensByUserId(Long userId) {
        List<Token> tokenList = this.tokenRepository.findAllTokenByUserId(userId);
        if (!tokenList.isEmpty()) {
            for (Token token: tokenList) {
                token.setExpiredByLogout(true);
                this.tokenRepository.save(token);
            }
        }
    }
}
