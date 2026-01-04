package com.hrmFirm.modules.auth.adapter.output.security;

import com.hrmFirm.modules.auth.domain.AuthToken;
import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.output.TokenProviderPort;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtTokenProviderAdapter implements TokenProviderPort {

    private final String secret = "0123456789ABCDEF0123456789ABCDEF";

    @Override
    public AuthToken generateToken(AuthUser user) {
        String token = Jwts.builder()
                .setSubject(user.id().toString())
                .claim("email", user.email())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000))
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();

        return new AuthToken(token);
    }
}
