package com.hrmf.hrms_backend.dto.leave;

import com.hrmf.hrms_backend.enums.LeaveStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployerLeaveFilterRequest {
    private int year;
    private int month;
    private LeaveStatus status;
    private String search;
    private int page = 0;
    private int size = 10;
    private String sortBy = "createdAt";
    private String sortDirection = "DESC";
}