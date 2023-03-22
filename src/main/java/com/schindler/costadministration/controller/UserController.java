package com.schindler.costadministration.controller;

import com.schindler.costadministration.dto.*;
import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.ModifyUserModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.service.UserService;
import com.schindler.costadministration.validation.usermodel.RegisterUserModelValidator;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final RegisterUserModelValidator registerUserModelValidator;
    private final UserService userService;
    @Autowired
    public UserController(RegisterUserModelValidator registerUserModelValidator, UserService userService) {
        this.registerUserModelValidator = registerUserModelValidator;
        this.userService = userService;
    }

    @InitBinder
    public void registerUserBinder(WebDataBinder webDataBinder) {
        webDataBinder.addValidators(this.registerUserModelValidator);
    }

    @PostMapping("/register")
    public ResponseEntity<VerificationDto> registerUser(@RequestBody @Valid RegisterUserModel userModel) throws MessagingException, UnsupportedEncodingException {
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
