package com.hrmFirm.common.validation;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public final class ValidationPatterns {

    public static final String STRONG_PASSWORD =
            "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).{8,}$";

}