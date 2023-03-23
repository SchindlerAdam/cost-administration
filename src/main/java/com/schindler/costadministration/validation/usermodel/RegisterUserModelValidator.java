package com.schindler.costadministration.validation.usermodel;

import com.schindler.costadministration.model.RegisterUserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class RegisterUserModelValidator implements Validator {

    private static final String EMAIL_FIELD = "email";
    private static final String PASSWORD_FIELD = "password";
    private static final String USERNAME_FIELD = "username";

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

        if (this.registerUserModelValidatorService.checkIfFieldIsEmptyOrBlank(registerUserModel.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, "email.empty");
        }

        if (this.registerUserModelValidatorService.checkIfFieldIsEmptyOrBlank(registerUserModel.getPassword())) {
            errors.rejectValue(PASSWORD_FIELD, "password.empty");
        }

        if (this.registerUserModelValidatorService.checkIfFieldIsEmptyOrBlank(registerUserModel.getUsername())) {
            errors.rejectValue(USERNAME_FIELD, "username.empty");
        }

        if (this.registerUserModelValidatorService.checkIfEmailIsExist(registerUserModel.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, "email.already.exists");
        }

        if (!this.registerUserModelValidatorService.checkIfEmailContainsAtSymbol(registerUserModel.getEmail())) {
            errors.rejectValue(EMAIL_FIELD, "email.invalid.format");
        }

        if (!this.registerUserModelValidatorService.checkIfPasswordIsValid(registerUserModel.getPassword())) {
            errors.rejectValue(PASSWORD_FIELD, "password.invalid");
        }
    }
}
