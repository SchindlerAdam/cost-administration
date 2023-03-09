package com.schindler.costadministration.controller;

import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.dto.TokenDto;
import com.schindler.costadministration.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<TokenDto> registerUser(@RequestBody RegisterUserModel userModel) {
        return ResponseEntity.ok(userService.registerUser(userModel));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(@RequestBody AuthModel authModel) {
        return ResponseEntity.ok(userService.authenticate(authModel));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test API for JWT is working!");
    }


}
