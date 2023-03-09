package com.schindler.costadministration.service;

import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.dto.TokenDto;
import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.jwt.JwtService;
import com.schindler.costadministration.model.TokenModel;
import com.schindler.costadministration.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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
        System.out.println(user.getRole());
        this.userRepository.save(user);
        String jwtToken = this.jwtService.generateToken(user);
        saveToken(user, jwtToken);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }

    public TokenDto authenticate(AuthModel authModel) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authModel.getEmail(), authModel.getPassword()));
        var user = this.userRepository.findUserByEmail(authModel.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Can not find a user with this email address!"));
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
}
