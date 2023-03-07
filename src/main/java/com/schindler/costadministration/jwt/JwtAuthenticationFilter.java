package com.schindler.costadministration.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
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
                if (isTokenIsValid(jwtToken, userDetails)) {
                    createAuthTokenForSecurityContextHolder(userDetails, request);
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

    private void createAuthTokenForSecurityContextHolder(UserDetails userDetails, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        updateSecurityContextHolder(authenticationToken);
    }

    private void updateSecurityContextHolder(UsernamePasswordAuthenticationToken authenticationToken) {
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
    }


}
