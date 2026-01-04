package com.hrmFirm.common.mapper;

import com.hrmFirm.common.dto.FieldErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;

@Component
public class ErrorFieldResponseMapper {
    public static FieldErrorResponse mapToFieldErrorResponse(FieldError error) {
        return FieldErrorResponse.builder()
                .field(error.getField())
                .message(error.getDefaultMessage())
                .rejectedValue(error.getRejectedValue())
                .code(error.getCode())
                .build();
    }
}
