package com.schindler.costadministration.exception;

import com.schindler.costadministration.dto.ExceptionDto;
import com.schindler.costadministration.exception.exceptions.*;
import com.schindler.costadministration.validation.usermodel.ValidationError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final MessageSource messageSource;

    @Autowired
    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    private ValidationError processFieldErrors(List<FieldError> fieldErrors) {
        ValidationError validationError = new ValidationError();
        for (FieldError fieldError : fieldErrors) {
            validationError.addFieldError(fieldError.getField(), messageSource.getMessage(fieldError, Locale.getDefault()));
        }
        return validationError;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        processFieldErrors(fieldErrors)
                );
    }


    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ExceptionDto> handleAuthenticationException(AuthenticationException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserAlreadyExistException.class)
    public ResponseEntity<ExceptionDto> handleUserAlreadyExistException(UserAlreadyExistException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleUserNotFoundException(UserNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

    @ExceptionHandler(ModifyUserException.class)
    public ResponseEntity<ExceptionDto> handleModifyUserException(ModifyUserException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

    @ExceptionHandler(DeleteUserException.class)
    public ResponseEntity<ExceptionDto> handleDeleteUserException(DeleteUserException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ExceptionDto> handleUsernameNotFoundException(UsernameNotFoundException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ExceptionDto> handleDefaultException(Throwable exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(
                        ExceptionDto.builder()
                                .errorCode(HttpStatus.BAD_REQUEST)
                                .errorDetails(exception.getLocalizedMessage())
                                .build()
                );
    }

}
