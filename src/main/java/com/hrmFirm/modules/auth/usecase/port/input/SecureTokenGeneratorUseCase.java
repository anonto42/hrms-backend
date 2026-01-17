package com.hrmFirm.modules.auth.usecase.port.input;

public interface SecureTokenGeneratorUseCase {
    String generateToken(int byteLength);
}
