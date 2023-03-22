package com.schindler.costadministration.service;



import com.schindler.costadministration.dto.*;
import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.entities.VerificationCode;
import com.schindler.costadministration.exception.exceptions.AuthenticationException;
import com.schindler.costadministration.exception.exceptions.DeleteUserException;
import com.schindler.costadministration.exception.exceptions.ModifyUserException;
import com.schindler.costadministration.exception.exceptions.UserNotFoundException;
import com.schindler.costadministration.jwt.JwtService;
import com.schindler.costadministration.model.*;
import com.schindler.costadministration.repository.UserRepository;
import com.schindler.costadministration.verification.VerificationService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.UUID;

@Service
@Transactional
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final TokenService tokenService;

    private final VerificationService verificationService;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenService tokenService,
            VerificationService verificationService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
        this.verificationService = verificationService;
    }

    public VerificationDto registerUser(RegisterUserModel userModel) throws MessagingException, UnsupportedEncodingException {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        User user = new User(userModel);
        this.userRepository.save(user);
        VerificationCodeModel verificationCodeModel = VerificationCodeModel.builder()
                .code(UUID.randomUUID().toString())
                .creationDate(new Date())
                .user(user)
                .build();
        this.verificationService.saveVerificationCode(verificationCodeModel);
        this.verificationService.sendVerificationEmail(verificationCodeModel);
        return VerificationDto.builder()
                .message("Registration was successful! Please check the verification email and finish the registration process!")
                .build();
    }

    public TokenDto verifyUser(String verificationCode) {
        VerificationCode code = this.verificationService.getVerificationCode(verificationCode);
        User user = this.userRepository.findNotVerifiedUserByEmail(code.getUser().getEmail()).orElseThrow(UserNotFoundException::new);
        user.setIsVerified(true);
        this.userRepository.save(user);
        String jwtToken = this.jwtService.generateToken(user);
        saveToken(user, jwtToken);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }

    public TokenDto authenticate(AuthModel authModel) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authModel.getEmail(), authModel.getPassword()));
        User user = this.userRepository.findVerifiedUserByEmail(authModel.getEmail()).orElseThrow(AuthenticationException::new);
        String jwtToken = this.jwtService.generateToken(user);
        saveToken(user, jwtToken);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }

    private void saveToken(User user, String token) {
        TokenModel tokenModel = TokenModel.builder()
                .token(token)
                .isExpiredByLogout(false)
                .user(user)
                .build();
        this.tokenService.saveToken(tokenModel);
    }

    public ModifyUserDto modifyUser(ModifyUserModel modifyUserModel, HttpServletRequest request) {
        String token = getTokenFromRequestHeader(request);
        String email = getEmailFromToken(token);
        User user = this.userRepository.findVerifiedUserByEmail(email).orElseThrow(ModifyUserException::new);
        user.setUsername(modifyUserModel.getNewUsername());
        user.setPassword(passwordEncoder.encode(modifyUserModel.getNewPassword()));
        this.userRepository.save(user);
        return ModifyUserDto.builder()
                .message("Modification was successful!")
                .build();
    }


    public UserDetailsDto getUserDetails(HttpServletRequest request) {
        String token = getTokenFromRequestHeader(request);
        String email = getEmailFromToken(token);
        User user = this.userRepository.findVerifiedUserByEmail(email).orElseThrow(UserNotFoundException::new);
        return UserDetailsDto.builder()
                .userId(user.getUserId())
                .username((user.getUsername()))
                .password(user.getPassword())
                .email(user.getEmail())
                .balance(user.getBalance())
                .costList(user.getCostList())
                .goalList(user.getGoalList())
                .build();
    }


    public DeleteUserDto deleteUser(HttpServletRequest request) {
        String token = getTokenFromRequestHeader(request);
        String email = getEmailFromToken(token);
        User user = this.userRepository.findVerifiedUserByEmail(email).orElseThrow(DeleteUserException::new);
        this.tokenService.expireTokensByUserId(user.getUserId());
        user.setIsDeleted(true);
        this.userRepository.save(user);
        return DeleteUserDto.builder()
                .message("User has been deleted successfully!")
                .build();
    }


    public String getTokenFromRequestHeader(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER).substring(7);
    }

    public String getEmailFromToken(String token) {
        return this.jwtService.extractUserEmailFromToken(token);
    }
}
