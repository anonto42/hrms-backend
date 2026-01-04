package com.hrmFirm.modules.auth.usecase.port.output;

import com.hrmFirm.modules.auth.domain.AuthToken;
import com.hrmFirm.modules.auth.domain.AuthUser;

public interface TokenProviderPort {
    AuthToken generateToken(AuthUser user);
}