package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.usecase.port.command.VerifyOtpCommand;

public interface VerifyOtpUseCase {
    String verifyOtp(VerifyOtpCommand verifyOtpCommand);
}
