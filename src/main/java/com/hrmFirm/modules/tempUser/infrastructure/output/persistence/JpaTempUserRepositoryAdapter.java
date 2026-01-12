package com.hrmFirm.modules.tempUser.infrastructure.output.persistence;

import com.hrmFirm.common.exception.CustomException;
import com.hrmFirm.modules.tempUser.domain.TempUser;
import com.hrmFirm.modules.tempUser.infrastructure.output.persistence.mapper.TempUserMapper;
import com.hrmFirm.modules.tempUser.usecase.port.output.TempUserRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@AllArgsConstructor
public class JpaTempUserRepositoryAdapter implements TempUserRepositoryPort {

    private final SpringDataTempUserRepository repository;
    private final TempUserMapper userPersistenceMapper;

    @Override
    @Transactional
    public TempUser save(TempUser user) {
        return userPersistenceMapper
                .toDomain(
                        repository.save(
                                userPersistenceMapper.toEntity(user)
                        )
                );
    }

    @Override
    @Transactional
    public void delete(String email) {
        repository
                .delete(
                    repository
                            .findByEmail(email)
                                .orElseThrow(()-> new CustomException("User not found to delete form temp DB", HttpStatus.NOT_FOUND))
                );
    }

    @Override
    @Transactional
    public Optional<TempUser> findByEmail(String email) {
        return repository
                .findByEmail(email)
                .map(userPersistenceMapper::toDomain);
    }

    @Override
    public boolean existByEmail(String email) {
        return repository.existsByEmail(email);
    }
}
