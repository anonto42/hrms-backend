package com.hrmFirm.modules.user.domain;

import com.hrmFirm.common.enums.UserRole;
import com.hrmFirm.common.enums.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record User (
   UUID id,
   String name,
   String email,
   String password,
   String imageUrl,
   UserRole role,
   UserStatus status,
   UUID createdBy,
   LocalDateTime createdAt,
   LocalDateTime updatedAt
) {}