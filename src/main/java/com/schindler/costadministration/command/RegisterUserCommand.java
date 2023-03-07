package com.schindler.costadministration.command;


import lombok.Data;

@Data
public class RegisterUserCommand {
    private String username;
    private String password;
    private String email;
}
