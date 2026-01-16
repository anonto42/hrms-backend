package com.hrmFirm.modules.auth.infrastructure.output.security;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.concurrent.ThreadLocalRandom;

@Component
public class SecureOtpGenerator {
    private static final String NUMERIC = "0123456789";
    private static final String ALPHANUMERIC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    public String generateNumericOtp(int length) {
        return generateOtp(NUMERIC, length);
    }

    public String generateAlphanumericOtp(int length) {
        return generateOtp(ALPHANUMERIC, length);
    }

    private String generateOtp(String characters, int length) {
        StringBuilder otp = new StringBuilder(length);

        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 0; i < length; i++) {
            otp.append(characters.charAt(random.nextInt(characters.length())));
        }

        return otp.toString();
    }

    // Generate cryptographically secure random
    public String generateSecureToken(int bytes) {
        byte[] tokenBytes = new byte[bytes];
        SECURE_RANDOM.nextBytes(tokenBytes);
        return bytesToHex(tokenBytes);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
