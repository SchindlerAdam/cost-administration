package com.schindler.costadministration.validation.usermodel;

import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class RegisterUserModelValidatorServiceTest {

    @Mock
    private UserRepository userRepository;
    private RegisterUserModelValidatorService underTest;

    @BeforeEach
    void init() {
        this.underTest = new RegisterUserModelValidatorService(userRepository);
    }

    @ParameterizedTest
    @ValueSource(strings = {"   "})
    void emailFieldShouldEmpty(String email) {
        boolean expected = underTest.checkIfFieldIsEmptyOrBlank(email);
        assertThat(expected).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test.com"})
    void emailFieldShouldNotEmpty(String email) {
        boolean expected = underTest.checkIfFieldIsEmptyOrBlank(email);
        assertThat(expected).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test.com"})
    void shouldFindUserWithEmail(String email) {
        // WHEN
        when(userRepository.findVerifiedUserByEmail(email))
                .thenReturn(Optional.of(new User()));


        boolean expected = underTest.checkIfEmailIsExist(email);

        // THEN
        assertThat(expected).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test.com"})
    void shouldNotFindUserWithEmail(String email) {
        // WHEN
        when(userRepository.findVerifiedUserByEmail(email))
                .thenReturn(Optional.empty());


        boolean expected = underTest.checkIfEmailIsExist(email);

        // THEN
        assertThat(expected).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test.com"})
    void emailShouldContainsAtSymbol(String email) {
        assertThat(underTest.checkIfEmailContainsAtSymbol(email)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"testvtest.com"})
    void emailShouldNotContainsAtSymbol(String email) {
        assertThat(underTest.checkIfEmailContainsAtSymbol(email)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test1", "Test1234"})
    void passwordShouldContainsAtLeastOneDigit(String password) {
        assertThat(underTest.isPasswordContainsAtLeastOneDigit(password)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", "Test&!"})
    void passwprdShouldNotContainsDigit(String password) {
        assertThat(underTest.isPasswordContainsAtLeastOneDigit(password)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test", "TeSt&!"})
    void passwordShouldContainsAtLeastOneUppercaseLetter(String password) {
        assertThat(underTest.isPasswordContainsAtLeastOneUppercaseLetter(password)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"test", "pa33word"})
    void passwordShouldNotContainsAtLeastOneUppercaseLetter(String password) {
        assertThat(underTest.isPasswordContainsAtLeastOneUppercaseLetter(password)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"pass!", "p@ssword", "pa\\ss", "pas%sword", "pass'rd", "password~", "p?ssword", "pa{}rd", "passw()rd!"})
    void passwordShouldContainsAtLeastOneSpecialCharacter(String password) {
        assertThat(underTest.isPasswordContainsAtLeastOneSpecialCharacter(password)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"password1234", "PAssWord43266"})
    void passwordShouldNotContainsAtLeastOneSpecialCharacter(String password) {
        assertThat(underTest.isPasswordContainsAtLeastOneSpecialCharacter(password)).isFalse();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password123!", "p@ssWord425"})
    void passwordShouldBeValid(String password) {
        assertThat(underTest.checkIfPasswordIsValid(password)).isTrue();
    }

    @ParameterizedTest
    @ValueSource(strings = {"Password!", "passWord425"})
    void passwordShouldBeInValid(String password) {
        assertThat(underTest.checkIfPasswordIsValid(password)).isFalse();
    }


}