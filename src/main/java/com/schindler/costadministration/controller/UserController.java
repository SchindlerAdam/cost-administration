package com.schindler.costadministration.controller;

import com.schindler.costadministration.dto.DeleteUserDto;
import com.schindler.costadministration.dto.ModifyUserDto;
import com.schindler.costadministration.dto.UserDetailsDto;
import com.schindler.costadministration.model.AuthModel;
import com.schindler.costadministration.model.ModifyUserModel;
import com.schindler.costadministration.model.RegisterUserModel;
import com.schindler.costadministration.dto.TokenDto;
import com.schindler.costadministration.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
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
        return ResponseEntity.ok(this.userService.registerUser(userModel));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<TokenDto> authenticate(@RequestBody AuthModel authModel) {
        return ResponseEntity.ok(this.userService.authenticate(authModel));
    }

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test API for JWT is working!");
    }

    @PutMapping("/modify")
    public ResponseEntity<ModifyUserDto> modifyUser(@RequestBody ModifyUserModel modifyUserModel, HttpServletRequest request) {
        return ResponseEntity.ok(this.userService.modifyUser(modifyUserModel, request));
    }
    @GetMapping("/user-details")
    public ResponseEntity<UserDetailsDto> getUserDetails(HttpServletRequest request) {
        return ResponseEntity.ok(this.userService.getUserDetails(request));
    }

    @PostMapping("/delete")
    public ResponseEntity<DeleteUserDto> deleteUser(HttpServletRequest request) {
        return ResponseEntity.ok(this.userService.deleteUser(request));
    }




}
