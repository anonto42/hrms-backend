package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.usecase.port.command.VerifyOtpCommand;

public interface VerifyOtpUseCase {
    boolean verifyOtp(VerifyOtpCommand verifyOtpCommand);
}
