package com.schindler.costadministration.validation.usermodel;

import com.schindler.costadministration.model.RegisterUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterUserModelValidator implements Validator {

    private final RegisterUserModelValidatorService registerUserModelValidatorService;

    @Autowired
    public RegisterUserModelValidator(RegisterUserModelValidatorService registerUserModelValidatorService) {
        this.registerUserModelValidatorService = registerUserModelValidatorService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return RegisterUserModel.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        RegisterUserModel registerUserModel = (RegisterUserModel) target;

        if (this.registerUserModelValidatorService.checkIfEmailIsExist(registerUserModel.getEmail())) {
            errors.rejectValue("email", "email.already.exists");
        }

        if (!this.registerUserModelValidatorService.checkIfEmailContainsAtSymbol(registerUserModel.getEmail())) {
            errors.rejectValue("email", "email.invalid.format");
        }

        if (!this.registerUserModelValidatorService.checkIfPasswordIsValid(registerUserModel.getPassword())) {
            errors.rejectValue("password", "password.invalid");
        }
    }
}
