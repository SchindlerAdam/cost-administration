package com.schindler.costadministration.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class JwtAuthenticationFilterTest {

    JwtAuthenticationFilter authenticationFilter;

    @BeforeEach
    void initJwtAuthFilter() {
        this.authenticationFilter = new JwtAuthenticationFilter();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bearer dasjdkhaskjdh12312323hdasiduhasiudhasd"})
    void authHeaderStartsWithBearer(String authHeader) {
        assertFalse(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }


    @ParameterizedTest
    @ValueSource(strings = {"dasjdkhaskjdh12312323hdasiduhasiudhasd"})
    void authHeaderNotStartsWithBearer(String authHeader) {
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @ParameterizedTest
    @ValueSource(strings = {"bearer dasjdkhaskjdh12312323hdasiduhasiudhasd"})
    void authHeaderStartsWithLowerCaseBearer(String authHeader) {
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Bearerdasjdkhaskjdh12312323hdasiduhasiudhasd"})
    void authHeaderNotContainsSpaceBetweenBearer(String authHeader) {
        assertTrue(this.authenticationFilter.isJwtTokenNotPresent(authHeader));
    }

    @ParameterizedTest
    @NullSource
    void authHeaderIsNull(String authHeader) {;
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