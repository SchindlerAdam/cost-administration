package com.schindler.costadministration.controller;

import com.schindler.costadministration.command.AuthCommand;
import com.schindler.costadministration.command.RegisterUserCommand;
import com.schindler.costadministration.dto.TokenDto;
import com.schindler.costadministration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> registerUser(@RequestBody RegisterUserCommand userCommand) {
        return ResponseEntity.ok(userService.registerUser(userCommand));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(@RequestBody AuthCommand authCommand) {
        return ResponseEntity.ok(userService.authenticate(authCommand));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test API for JWT is working!");
    }


}
