package com.hrmf.user_service.dto;

import com.hrmf.user_service.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryDTO {
    private UUID id;
    private String email;
    private String imageUrl;
    private User.UserStatus status;
    private LocalDateTime createdAt;
}