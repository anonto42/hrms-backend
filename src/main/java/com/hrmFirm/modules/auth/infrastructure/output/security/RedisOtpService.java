package com.hrmFirm.modules.auth.infrastructure.output.security;

import com.hrmFirm.common.dto.OtpDataResponse;
import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.common.exception.RateLimitExceededException;
import com.hrmFirm.common.properties.OtpProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.chrono.ChronoLocalDateTime;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisOtpService {

    private static final String OTP_KEY_PREFIX = "otp:";
    private static final String OTP_GEN_ATTEMPTS_PREFIX = "otp:gen:attempts:";
    private static final String OTP_VERIFY_ATTEMPTS_PREFIX = "otp:verify:attempts:";
    private static final String OTP_RESET_TOKEN_PREFIX = "otp:reset:token:";

    private final RedisTemplate<String, Object> redisTemplate;
    private final OtpProperties otpProperties;
    private final SecureOtpGenerator otpGenerator;

    public String generateOtp(String email) {
        String emailKey = normalizeEmail(email);

        // Check rate limiting for generation
        String genAttemptKey = OTP_GEN_ATTEMPTS_PREFIX + emailKey;
        Integer genAttempts = (Integer) redisTemplate.opsForValue().get(genAttemptKey);

        if (genAttempts != null && genAttempts >= otpProperties.getMaxGenerationAttempts()) {
            Long ttl = redisTemplate.getExpire(genAttemptKey, TimeUnit.SECONDS);
            throw new RateLimitExceededException(
                    String.format("Too many OTP requests. Please try again after %d seconds", ttl)
            );
        }

        // Check cooldown period
        String otpKey = OTP_KEY_PREFIX + emailKey;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(otpKey))) {
            Long remainingTtl = redisTemplate.getExpire(otpKey, TimeUnit.SECONDS);
            if (remainingTtl != null && remainingTtl > otpProperties.getExpiration() - 60) {
                throw new RateLimitExceededException(
                        String.format("Please wait %d seconds before requesting new OTP", remainingTtl)
                );
            }
        }

        // Generate OTP
        String otp = otpProperties.isNumericOnly()
                ? otpGenerator.generateNumericOtp(otpProperties.getLength())
                : otpGenerator.generateAlphanumericOtp(otpProperties.getLength());

        // Store OTP in Redis with TTL
        OtpDataResponse otpData = new OtpDataResponse(otp, LocalDateTime.from(Instant.now().plusSeconds(otpProperties.getExpiration())));
        redisTemplate.opsForValue().set(otpKey, otpData, otpProperties.getExpiration(), TimeUnit.SECONDS);

        // Increment attempt counter
        if (genAttempts == null) {
            redisTemplate.opsForValue().set(
                    genAttemptKey,
                    1,
                    otpProperties.getResetAttemptAfter().getSeconds(),
                    TimeUnit.SECONDS
            );
        } else {
            redisTemplate.opsForValue().increment(genAttemptKey);
        }

        log.info("Generated OTP for {} with TTL {}s", email, otpProperties.getExpiration());
        return otp;
    }

    public boolean verifyOtp(String email, String otp) {
        String emailKey = normalizeEmail(email);
        String otpKey = OTP_KEY_PREFIX + emailKey;
        String verifyAttemptKey = OTP_VERIFY_ATTEMPTS_PREFIX + emailKey;

        // Check rate limiting for verification
        Integer verifyAttempts = (Integer) redisTemplate.opsForValue().get(verifyAttemptKey);
        if (verifyAttempts != null && verifyAttempts >= otpProperties.getMaxVerificationAttempts()) {
            throw new RateLimitExceededException("Too many verification attempts. Account temporarily locked.");
        }

        OtpDataResponse otpData = (OtpDataResponse) redisTemplate.opsForValue().get(otpKey);

        if (otpData == null) {
            log.warn("No OTP found for email: {}", email);
            incrementVerificationAttempts(verifyAttemptKey);
            return false;
        }

        if (otpData.expiresAt().isBefore(ChronoLocalDateTime.from(Instant.now()))) {
            redisTemplate.delete(otpKey);
            log.warn("OTP expired for email: {}", email);
            incrementVerificationAttempts(verifyAttemptKey);
            return false;
        }

        boolean isValid = otpData.otp().equals(otp);

        if (isValid) {
            // Generate password reset token
            String resetToken = otpGenerator.generateAlphanumericOtp(32);
            String resetTokenKey = OTP_RESET_TOKEN_PREFIX + emailKey;

            // Store reset token with shorter expiration (10 minutes)
            redisTemplate.opsForValue().set(
                    resetTokenKey,
                    resetToken,
                    600,
                    TimeUnit.SECONDS
            );

            // Cleanup
            redisTemplate.delete(otpKey);
            redisTemplate.delete(verifyAttemptKey);
            redisTemplate.delete(OTP_GEN_ATTEMPTS_PREFIX + emailKey);

            log.info("OTP verified successfully for email: {}", email);
        } else {
            log.warn("Invalid OTP for email: {}", email);
            incrementVerificationAttempts(verifyAttemptKey);
        }

        return isValid;
    }

    public String getResetToken(String email) {
        String emailKey = normalizeEmail(email);
        String resetTokenKey = OTP_RESET_TOKEN_PREFIX + emailKey;

        String token = (String) redisTemplate.opsForValue().get(resetTokenKey);
        if (token == null) {
            throw new CustomException("Reset token expired or not found", HttpStatus.BAD_REQUEST);
        }

        return token;
    }

    public boolean validateResetToken(String email, String token) {
        String emailKey = normalizeEmail(email);
        String resetTokenKey = OTP_RESET_TOKEN_PREFIX + emailKey;

        String storedToken = (String) redisTemplate.opsForValue().get(resetTokenKey);
        boolean isValid = token.equals(storedToken);

        if (isValid) {
            redisTemplate.delete(resetTokenKey);
        }

        return isValid;
    }

    private void incrementVerificationAttempts(String key) {
        Integer attempts = (Integer) redisTemplate.opsForValue().get(key);
        if (attempts == null) {
            redisTemplate.opsForValue().set(
                    key,
                    1,
                    otpProperties.getResetAttemptAfter().getSeconds(),
                    TimeUnit.SECONDS
            );
        } else {
            redisTemplate.opsForValue().increment(key);
        }
    }

    private String normalizeEmail(String email) {
        return email.toLowerCase().trim();
    }
}