package com.schindler.costadministration.jwt;

import com.schindler.costadministration.exception.exceptions.AuthenticationException;
import com.schindler.costadministration.repository.TokenRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@NoArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private UserDetailsService userDetailsService;

    private TokenRepository tokenRepository;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.tokenRepository = tokenRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (isJwtTokenNotPresent(authHeader)) {
            filterChain.doFilter(request, response);
        }
        else {
            String jwtToken = extractTokenFromHeader(authHeader);
            String userEmail = jwtService.extractUserEmailFromToken(jwtToken);
            if (isUserEmailPresentedAndUserIsNotAuthenticatedYet(userEmail)) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                if (isTokenIsValid(jwtToken, userDetails) && isTokenNotExpiredByLogout(jwtToken)) {
                    updateSecurityContextHolder(userDetails, request);
                }
            }
            filterChain.doFilter(request, response);
        }
    }

    public boolean isJwtTokenNotPresent(String authHeader) {
        return authHeader == null || !authHeader.startsWith("Bearer ");
    }

    public String extractTokenFromHeader(String authHeader) {
        return authHeader.substring(7);
    }

    private boolean isUserEmailPresentedAndUserIsNotAuthenticatedYet(String userEmail) {
        return userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null;
    }

    private boolean isTokenIsValid(String token, UserDetails userDetails) {
        return this.jwtService.isTokenValid(token, userDetails);
    }

    private boolean isTokenNotExpiredByLogout(String jwtToken) {
        return this.tokenRepository.findTokenByToken(jwtToken).map(token -> !token.isExpiredByLogout()).orElse(false);
    }

    private void updateSecurityContextHolder(UserDetails userDetails, HttpServletRequest request) {
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        securityContext.setAuthentication(authenticationToken);
        SecurityContextHolder.setContext(securityContext);
    }


}
