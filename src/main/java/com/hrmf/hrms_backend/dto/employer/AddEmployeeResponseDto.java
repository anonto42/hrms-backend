package com.hrmf.hrms_backend.dto.employer;

import com.hrmf.hrms_backend.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddEmployeeResponseDto {
    private String id;
    private String name;
    private String gender;
    private String email;
    private UserRole role;
    private String employeeRole;
}
