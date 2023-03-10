package com.schindler.costadministration.service;

import com.schindler.costadministration.entities.Token;
import com.schindler.costadministration.repository.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;

@Service
public class LogOutService implements LogoutHandler {

    private final TokenRepository tokenRepository;

    @Autowired
    public LogOutService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String authHeader = request.getHeader("Authorization");
        if (isAuthHeaderValid(authHeader)) {
            String jwtToken = extractJwtTokenFromHeader(authHeader);
            Token token = this.tokenRepository.findTokenByToken(jwtToken).orElse(null);
            if (token != null) {
                token.setExpiredByLogout(true);
                this.tokenRepository.save(token);
            }
        }
    }

    private boolean isAuthHeaderValid(String authHeader) {
        return authHeader != null && authHeader.startsWith("Bearer ");
    }

    private String extractJwtTokenFromHeader(String authHeader) {
        return authHeader.substring(7);
    }
}
