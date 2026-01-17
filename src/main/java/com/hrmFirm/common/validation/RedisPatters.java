package com.hrmFirm.common.validation;

public class RedisPatters {
    public static String OTP(String key) {
        return "otp:" + key;
    }

    public static String RESET_PASSWORD_TOKEN(String key) {
        return "reset_password_token:" + key;
    }
}
