package com.schindler.costadministration.controller;

import com.schindler.costadministration.dto.*;
import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.ModifyUserModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<VerificationDto> registerUser(@RequestBody RegisterUserModel userModel) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.userService.registerUser(userModel));
    }

    @GetMapping("/verification/{verificationCode}")
    public ResponseEntity<TokenDto> verifyUser(@PathVariable("verificationCode") String verificationCode) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.verifyUser(verificationCode));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(@RequestBody AuthModel authModel) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.authenticate(authModel));
    }

    @PutMapping("/modify")
    public ResponseEntity<ModifyUserDto> modifyUser(@RequestBody ModifyUserModel modifyUserModel, HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.modifyUser(modifyUserModel, request));
    }
    @GetMapping("/user-details")
    public ResponseEntity<UserDetailsDto> getUserDetails(HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.getUserDetails(request));
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteUserDto> deleteUser(HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(this.userService.deleteUser(request));
    }




}
