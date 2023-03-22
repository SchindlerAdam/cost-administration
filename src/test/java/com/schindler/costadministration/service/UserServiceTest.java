package com.schindler.costadministration.service;

import com.schindler.costadministration.dto.ModifyUserDto;
import com.schindler.costadministration.dto.UserDetailsDto;
import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.entities.VerificationCode;
import com.schindler.costadministration.exception.exceptions.UserNotFoundException;
import com.schindler.costadministration.jwt.JwtService;
import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.ModifyUserModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.model.VerificationCodeModel;
import com.schindler.costadministration.repository.UserRepository;
import com.schindler.costadministration.verification.VerificationService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Optional;

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
    void verifyUserMethodShouldInvokeFindNotVerifiedUserByEmail() {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        User user = new User(userModel);

        VerificationCodeModel model = VerificationCodeModel.builder()
                .code("randomVerificationCode")
                .creationDate(new Date())
                .user(user)
                .build();

        // WHEN

        when(verificationService.getVerificationCode(model.getCode()))
                .thenReturn(new VerificationCode(model));

        when(userRepository.findNotVerifiedUserByEmail(model.getUser().getEmail()))
                .thenReturn(Optional.of(user));

        underTest.verifyUser(model.getCode());

        ArgumentCaptor<String> codeArgumentCapture = ArgumentCaptor.forClass(String.class);
        verify(verificationService).getVerificationCode(codeArgumentCapture.capture());
        String capturedCode = codeArgumentCapture.getValue();

        // THEN
        assertThat(capturedCode).isEqualTo(model.getCode());
        verify(userRepository).findNotVerifiedUserByEmail(model.getUser().getEmail());

    }

    @Test
    void verifyUserMethodShouldThrowException() {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        User user = new User(userModel);

        VerificationCodeModel model = VerificationCodeModel.builder()
                .code("randomVerificationCode")
                .creationDate(new Date())
                .user(user)
                .build();

        // WHEN
        when(verificationService.getVerificationCode(model.getCode()))
                .thenReturn(new VerificationCode(model));

        // THEN
            assertThatThrownBy(() -> underTest.verifyUser(model.getCode()))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("Can not find a user with this email address!");
    }

    @Test
    void authenticateMethodShouldBeCalled() {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        User user = new User(userModel);

        AuthModel authModel = AuthModel.builder()
                .email("test@test.com")
                .password("test12345")
                .build();

        // WHEN
        when(userRepository.findVerifiedUserByEmail(authModel.getEmail()))
                .thenReturn(Optional.of(user));

        underTest.authenticate(authModel);

        // THEN
        verify(authenticationManager).authenticate(new UsernamePasswordAuthenticationToken(
                authModel.getEmail(),
                authModel.getPassword()
                )
        );
    }

    @Test
    void getTokenFromRequestHeader() {
        // GIVEN
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader("Authorization", "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");

        // WHEN
        String expected = underTest.getTokenFromRequestHeader(mockHttpServletRequest);

        // THEN
        assertThat(
                expected)
                .isEqualTo("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c");
    }

    @Test
    void getEmailFromToken() {
        // GIVEN
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJkcnNjaGluZGxlci5hZGFtQGdtYWlsLmNvbSIsImlhdCI6MTY3OTA2NTkzNywiZXhwIjoxNjc5MTUyMzM3fQ.kulove2RwXeFK1g-X8kfWQb9e8dO6F5fq74I21iIklQ";

        // WHEN
        when(jwtService.extractUserEmailFromToken(token))
                .thenReturn("drschindler.adam@gmail.com");

        // THEN
        assertThat(underTest.getEmailFromToken(token)).isEqualTo("drschindler.adam@gmail.com");
    }

    @Test
    void modifyUser() {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        User user = new User(userModel);

        ModifyUserModel model = new ModifyUserModel();
        model.setNewUsername("newUser");
        model.setNewPassword("newPassword");

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");

        // WHEN
        when(userRepository.findVerifiedUserByEmail(null))
                .thenReturn(Optional.of(user));

        ModifyUserDto modifyUserDto = underTest.modifyUser(model, request);
        // THEN
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser).isEqualTo(user);
        assertThat(modifyUserDto.getMessage()).isEqualTo("Modification was successful!");
    }


    @Test
    void getUserDetails() {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        User user = new User(userModel);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");

        // WHEN
        when(userRepository.findVerifiedUserByEmail(null))
                .thenReturn(Optional.of(user));

        UserDetailsDto userDetailsDto = underTest.getUserDetails(request);
        verify(userRepository).findVerifiedUserByEmail(null);
        assertThat(user.getUsername()).isEqualTo(userDetailsDto.getUsername());
    }

    @Test
    void deleteUser() {
        // GIVEN
        RegisterUserModel userModel = new RegisterUserModel();
        userModel.setEmail("test@test.com");
        userModel.setUsername("Test");
        userModel.setPassword("Test12345");
        User user = new User(userModel);

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer token123");

        // WHEN
        when(userRepository.findVerifiedUserByEmail(null))
                .thenReturn(Optional.of(user));
        underTest.deleteUser(request);
        // THEN
        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userArgumentCaptor.capture());
        User capturedUser = userArgumentCaptor.getValue();
        assertThat(capturedUser.getIsDeleted()).isTrue();
    }
}