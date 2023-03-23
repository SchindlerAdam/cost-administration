package com.schindler.costadministration.validation.usermodel;


import com.schindler.costadministration.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class RegisterUserModelValidatorService {

    private UserRepository userRepository;


    @Autowired
    public RegisterUserModelValidatorService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean checkIfEmailIsExist(String email) {
        return this.userRepository.findVerifiedUserByEmail(email).isPresent();
    }

    public boolean checkIfEmailContainsAtSymbol(String email) {
        return email.contains("@");
    }

    public boolean checkIfPasswordIsValid(String password) {
        return isPasswordContainsAtLeastOneDigit(password) && isPasswordContainsAtLeastOneUppercaseLetter(password) && isPasswordContainsAtLeastOneSpecialCharacter(password);
    }

    public boolean isPasswordContainsAtLeastOneDigit(String password) {
        int digitCounter = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isDigit(password.charAt(i))) {
                digitCounter++;
                break;
            }
        }
        return digitCounter > 0;
    }

    public boolean isPasswordContainsAtLeastOneUppercaseLetter(String password) {
        int upperCaseLetterCounter = 0;
        for (int i = 0; i < password.length(); i++) {
            if (Character.isUpperCase(password.charAt(i))) {
                upperCaseLetterCounter++;
                break;
            }
        }
        return upperCaseLetterCounter > 0;
    }

    public boolean isPasswordContainsAtLeastOneSpecialCharacter(String password) {
        char[] specialCharacters = {'!', '"', '#', '$', '%', '&', '\'', '(', ')', '*', '+', ',', '-', '/', '.', ':', ';', '<', '>', '?', '@', '[', ']', '\\', '^', '_', '`', '{', '}', '|', '~'};
        int specialCharacterCounter = 0;
        for (int i = 0; i < password.length(); i++) {
            for (char specialCharacter : specialCharacters) {
                if (password.charAt(i) == specialCharacter) {
                    specialCharacterCounter++;
                    break;
                }
            }
        }
        return specialCharacterCounter > 0;
    }

    public boolean checkIfFieldIsEmptyOrBlank(String field) {
        return field.isEmpty() || field.isBlank();
    }
}
