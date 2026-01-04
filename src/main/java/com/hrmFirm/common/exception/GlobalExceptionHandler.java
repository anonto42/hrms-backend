package com.hrmFirm.common.exception;

import com.hrmFirm.common.dto.ApiResponse;
import com.hrmFirm.common.dto.FieldErrorResponse;
import com.hrmFirm.common.mapper.ErrorFieldResponseMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(
            NoResourceFoundException ex,
            HttpServletRequest request) {

        String path = request.getRequestURI();

        if (path.startsWith("/api")) {
            ApiResponse<Void> response = ApiResponse.<Void>error(
                    "Endpoint not found",
                    "ENDPOINT_NOT_FOUND"
            ).withPath(path);

            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        ApiResponse<Void> response = ApiResponse.<Void>error(
                "Static resource not found",
                "STATIC_RESOURCE_NOT_FOUND"
        ).withPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLockingException(
            ObjectOptimisticLockingFailureException ex,
            HttpServletRequest request) {

        ApiResponse<Void> response = ApiResponse.<Void>error(
                "Row was updated or deleted by another transaction",
                HttpStatus.CONFLICT.name()
        ).withPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpServletRequest request) {

        String supportedMethods = ex.getSupportedHttpMethods() != null
                ? ex.getSupportedHttpMethods().toString()
                : "N/A";

        ApiResponse<Void> response = ApiResponse.<Void>error(
                "Method not allowed. Supported methods: " + supportedMethods,
                "METHOD_NOT_ALLOWED"
        ).withPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(response);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<Void>> handleCustomException(
            CustomException ex,
            HttpServletRequest request) {

        ApiResponse<Void> response = ApiResponse.<Void>error(
                ex.getCustomMessage(),
                ex.getStatus().name()
        ).withPath(request.getRequestURI());

        return new ResponseEntity<>(response, ex.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGlobalException(
            Exception ex,
            HttpServletRequest request) {

        ApiResponse<Void> response = ApiResponse.<Void>error(
                "An unexpected error occurred",
                "INTERNAL_SERVER_ERROR"
        ).withPath(request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<FieldErrorResponse>>> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request) {

        List<FieldErrorResponse> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> new FieldErrorResponse(
                        error.getField(),
                        error.getDefaultMessage(),
                        error.getRejectedValue() != null ? error.getRejectedValue() : "null",
                        HttpStatus.FAILED_DEPENDENCY.toString()
                ))
                .collect(Collectors.toList());

        ApiResponse<List<FieldErrorResponse>> response = ApiResponse.<List<FieldErrorResponse>>builder()
                .success(false)
                .message("Validation failed")
                .errorCode("VALIDATION_ERROR")
                .data(fieldErrors)
                .path(request.getRequestURI())
                .timestamp(String.valueOf(LocalDateTime.now()))
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
