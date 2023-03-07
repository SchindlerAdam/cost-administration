package com.schindler.costadministration.service;

import com.schindler.costadministration.command.AuthCommand;
import com.schindler.costadministration.command.RegisterUserCommand;
import com.schindler.costadministration.dto.TokenDto;
import com.schindler.costadministration.entities.User;
import com.schindler.costadministration.jwt.JwtService;
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

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public TokenDto registerUser(RegisterUserCommand userCommand) {
        userCommand.setPassword(passwordEncoder.encode(userCommand.getPassword()));
        var user = new User(userCommand);
        System.out.println(user.getRole());
        this.userRepository.save(user);
        var jwtToken = this.jwtService.generateToken(user);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }

    public TokenDto authenticate(AuthCommand authCommand) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authCommand.getEmail(), authCommand.getPassword())
        );
        var user = this.userRepository.findUserByEmail(authCommand.getEmail()).orElseThrow(() -> new UsernameNotFoundException("Can not find a user with this email address!"));
        var jwtToken = this.jwtService.generateToken(user);
        return TokenDto.builder()
                .token(jwtToken)
                .build();
    }
}
