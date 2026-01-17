package com.hrmFirm.common.validation;

import com.hrmFirm.common.enums.UserStatus;
import com.hrmFirm.common.exception.CustomException;
import org.springframework.http.HttpStatus;

public class AccountStatusCheck {
    public static void checkAccountStatus(UserStatus status) {

        switch (status) {
            case TERMINATED:
                throw new CustomException(
                        "Your account has been terminated. Please contact support.",
                        HttpStatus.FORBIDDEN
                );

            case LOCKED:
                throw new CustomException(
                        "Account is locked due to too many failed attempts. Please reset your password or contact support.",
                        HttpStatus.FORBIDDEN
                );

            case SUSPENDED:
                throw new CustomException(
                        "Account is temporarily suspended. Please contact your administrator.",
                        HttpStatus.FORBIDDEN
                );

            case PENDING:
            case INVITATION_EXPIRED:
                throw new CustomException(
                        "Please verify your email address to activate your account.",
                        HttpStatus.FORBIDDEN
                );

            case RESIGNED:
                throw new CustomException(
                        "This account is no longer active.",
                        HttpStatus.FORBIDDEN
                );

            case PASSWORD_EXPIRED:
                throw new CustomException(
                        "Your password has expired. Please reset your password.",
                        HttpStatus.FORBIDDEN
                );

            case ACTIVE:
                break;

            default:
                throw new CustomException(
                        "Account status is invalid. Please contact support.",
                        HttpStatus.FORBIDDEN
                );
        }
    }
}
