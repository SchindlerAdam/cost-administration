package com.schindler.costadministration.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    JwtAuthenticationFilter authenticationFilter;

    @BeforeEach
    void initJwtAuthFilter() {
        this.authenticationFilter = new JwtAuthenticationFilter();
    }

    @Test
    void authHeaderStartsWithBearer() {
        String authHeader = "Bearer dasjdkhaskjdh12312323hdasiduhasiudhasd";
        assertFalse(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @Test
    void authHeaderNotStartsWithBearer() {
        String authHeader = "dasjdkhaskjdh12312323hdasiduhasiudhasd";
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @Test
    void authHeaderStartsWithLowerCaseBearer() {
        String authHeader = "bearer dasjdkhaskjdh12312323hdasiduhasiudhasd";
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @Test
    void authHeaderNotContainsSpaceBetweenBearer() {
        String authHeader = "Bearerdasjdkhaskjdh12312323hdasiduhasiudhasd";
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @Test
    void authHeaderIsNull() {
        String authHeader = null;
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @Test
    void extractTokenFromHeaderShouldPass() {
        String authHeader = "Bearer This is my token!";
        assertEquals("This is my token!", this.authenticationFilter.extractTokenFromHeader(authHeader));
    }

    @Test
    void extractTokenFromHeaderShouldFail() {
        String authHeader = "BearerThis is my token!";
        assertNotEquals("This is my token!", this.authenticationFilter.extractTokenFromHeader(authHeader));
    }


}