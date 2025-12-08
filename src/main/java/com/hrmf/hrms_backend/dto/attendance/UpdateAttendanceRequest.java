package com.hrmf.hrms_backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAttendanceRequest {
    private String adminNote;
    private String approvalStatus;
    private String rejectionReason;
}
