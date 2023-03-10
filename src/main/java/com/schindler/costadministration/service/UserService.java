package com.schindler.costadministration.service;

import com.schindler.costadministration.dto.DeleteUserDto;
import com.schindler.costadministration.dto.ModifyUserDto;
import com.schindler.costadministration.dto.UserDetailsDto;
import com.schindler.costadministration.exception.exceptions.AuthenticationException;
import com.schindler.costadministration.exception.exceptions.DeleteUserException;
import com.schindler.costadministration.exception.exceptions.ModifyUserException;
import com.schindler.costadministration.exception.exceptions.UserNotFoundException;
import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.ModifyUserModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.dto.TokenDto;
import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.jwt.JwtService;
import com.schindler.costadministration.model.TokenModel;
import com.schindler.costadministration.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    private final TokenService tokenService;

    private static final String AUTHORIZATION_HEADER = "Authorization";

    @Autowired
    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            AuthenticationManager authenticationManager,
            JwtService jwtService,
            TokenService tokenService)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.tokenService = tokenService;
    }

    public TokenDto registerUser(RegisterUserModel userModel) {
        userModel.setPassword(passwordEncoder.encode(userModel.getPassword()));
        var user = new User(userModel);
        this.userRepository.save(user);
        String jwtToken = this.jwtService.generateToken(user);
        saveToken(user, jwtToken);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }

    public TokenDto authenticate(AuthModel authModel) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authModel.getEmail(), authModel.getPassword()));
        var user = this.userRepository.findUserByEmail(authModel.getEmail()).orElseThrow(AuthenticationException::new);
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
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        String email = getEmailFromToken(token);
        User user = this.userRepository.findUserByEmail(email).orElseThrow(ModifyUserException::new);
        if (!modifyUserModel.getNewUsername().isBlank()) {
            user.setUsername(modifyUserModel.getNewUsername());
        }
        if (!modifyUserModel.getNewPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(modifyUserModel.getNewPassword()));
        }
        this.userRepository.save(user);
        return ModifyUserDto.builder()
                .message("Modification was successful!")
                .build();
    }

    public UserDetailsDto getUserDetails(HttpServletRequest request) {
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        String email = getEmailFromToken(token);
        User user = this.userRepository.findUserByEmail(email).orElseThrow(UserNotFoundException::new);
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
        String token = request.getHeader(AUTHORIZATION_HEADER).substring(7);
        String email = getEmailFromToken(token);
        User user = this.userRepository.findUserByEmail(email).orElseThrow(DeleteUserException::new);
        this.tokenService.expireTokensByUserId(user.getUserId());
        user.setIsDeleted(true);
        this.userRepository.save(user);
        return DeleteUserDto.builder()
                .message("User has been deleted successfully!")
                .build();
    }

    private String getEmailFromToken(String token) {
        return this.jwtService.extractUserEmailFromToken(token);
    }
}
