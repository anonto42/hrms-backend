package com.hrmFirm.modules.auth.usecase.port.input;

import com.hrmFirm.modules.auth.usecase.port.command.RefreshTokenCommand;
import com.hrmFirm.modules.auth.usecase.port.response.ResetTokenResponse;

public interface RefreshTokenUseCase {
    ResetTokenResponse refreshToken(RefreshTokenCommand command);
}
