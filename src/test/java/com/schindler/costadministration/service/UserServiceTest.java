package com.schindler.costadministration.service;

import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.exception.exceptions.UserAlreadyExistException;
import com.schindler.costadministration.jwt.JwtService;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.repository.UserRepository;
import com.schindler.costadministration.verification.VerificationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    private UserService underTest;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtService jwtService;

    @Mock
    private TokenService tokenService;

    @Mock
    private VerificationService verificationService;


    @BeforeEach
    void setUp() {
        underTest = new UserService(
                userRepository,
                passwordEncoder,
                authenticationManager,
                jwtService,
                tokenService,
                verificationService);
    }

    @Test
    void registerUserMethodShouldSaveUser() throws MessagingException, UnsupportedEncodingException {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        // WHEN
        underTest.registerUser(userModel);
        User user = new User(userModel);
        // THEN
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
    }

    @Test
    void registerUserMethodShouldThrowUserAlreadyExistsException() throws MessagingException, UnsupportedEncodingException {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");

        //WHEN
        underTest.isUserExisting(userModel.getEmail());
        given(userRepository.findNotVerifiedUserByEmail(userModel.getEmail()).isPresent())
                .willThrow(new UserAlreadyExistException());

        // THEN
        assertThatThrownBy(() -> underTest.registerUser(userModel))
                .isInstanceOf(UserAlreadyExistException.class)
                .hasMessageContaining("User with this email is already exists!");
    }

    @ParameterizedTest
    @ValueSource(strings = {"test@test.com"})
    void shouldCallFindNotVerifiedUserByEmail(String email) {
        // WHEN
        underTest.isUserExisting(email);
        // THEN
        verify(userRepository).findNotVerifiedUserByEmail(email);
    }

    @Test
    @Disabled
    void verifyUser() {
    }

    @Test
    @Disabled
    void authenticate() {
    }

    @Test
    @Disabled
    void modifyUser() {
    }

    @Test
    @Disabled
    void getUserDetails() {
    }

    @Test
    @Disabled
    void deleteUser() {
    }
}