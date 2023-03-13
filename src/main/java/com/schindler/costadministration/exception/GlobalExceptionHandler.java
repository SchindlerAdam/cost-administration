package com.schindler.costadministration.exception;

import com.schindler.costadministration.dto.ExceptionDto;
import com.schindler.costadministration.exception.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

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
    public ResponseEntity<ExceptionDto> handleUsernameNotFoundException(UserNotFoundException exception) {
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
