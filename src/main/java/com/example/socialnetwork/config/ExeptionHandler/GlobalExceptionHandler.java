package com.example.socialnetwork.config.ExeptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({BadCredentialsException.class})
    public ResponseEntity<String> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handleNotFoundError() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not Found");
    }
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<String> handleSecurityExpection() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Please authenticate to perform this function");
    }
}