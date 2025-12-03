package com.hrmf.user_service.dto;

import com.hrmf.user_service.entity.User;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDTO {
    @Email(message = "Invalid email format")
    private String email;
    private String imageUrl;
    private User.UserStatus status;
}