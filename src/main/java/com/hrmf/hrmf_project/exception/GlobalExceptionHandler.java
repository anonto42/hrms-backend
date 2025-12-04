package com.hrmf.hrmf_project.exception;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationException(MethodArgumentNotValidException ex) {

        log.error(ex.getMessage(), ex);

        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        return ResponseEntity
                .badRequest()
                .body(errors);
    }

    @ExceptionHandler({com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException.class})
    public ResponseEntity<Map<String, String>> handleUnknownFields(Exception ex) {

        log.error(ex.getMessage(), ex);

        Map<String, String> error = new HashMap<>();
        error.put("error", "Unknown field in request body: " + ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException ex) {

        log.error(ex.getMessage(), ex);

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<Map<String, String>> handleNoHandlerFoundException(NoHandlerFoundException ex) {

        log.error(ex.getMessage(), ex);

        Map<String, String> error = new HashMap<>();
        error.put("error", ex.getMessage());

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException ex) {

        Map<String, String> error = new HashMap<>();

        log.error(ex.getMessage(), error);

        error.put("error", "Authentication Failed");
        error.put("message", ex.getMessage());
        error.put("code", "AUTH_001");

        return ResponseEntity
                .badRequest()
                .body(error);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<?> handleAccessDeniedException(AccessDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Access Denied");
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("code", "AUTH_002");

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<?> handleSpringAccessDeniedException(org.springframework.security.access.AccessDeniedException ex) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Access Denied");
        errorResponse.put("message", "You don't have permission to access this resource");
        errorResponse.put("code", "AUTH_003");

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(Exception ex) {
        Map<String, String> errorResponse = new HashMap<>();

        errorResponse.put("error", "Internal Server Error");
        errorResponse.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}
