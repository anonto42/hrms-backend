package com.hrmf.hrms_backend.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Pattern;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = {})
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Pattern(regexp = "^(0[1-9]|1[0-2])-\\d{4}$", message = "Month-Year must be in MM-yyyy format")
public @interface ValidMonthYear {
    String message() default "Invalid month-year format. Use MM-yyyy";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}