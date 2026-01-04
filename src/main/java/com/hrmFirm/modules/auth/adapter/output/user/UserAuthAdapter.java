package com.hrmFirm.modules.auth.adapter.output.user;

import com.hrmFirm.modules.auth.domain.AuthUser;
import com.hrmFirm.modules.auth.usecase.port.output.UserAuthPort;
import com.hrmFirm.modules.user.adapter.output.persistence.SpringDataUserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@AllArgsConstructor
public class UserAuthAdapter implements UserAuthPort {

    private final SpringDataUserRepository repository;

    @Override
    public Optional<AuthUser> findByEmail(String email) {
        return repository.findByEmail(email)
                .map(entity ->
                        new AuthUser(
                                entity.getId(),
                                entity.getEmail(),
                                entity.getPassword()
                        )
                );
    }
}