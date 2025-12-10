package com.hrmf.hrms_backend.dto.subAdmin;

import com.hrmf.hrms_backend.enums.VerificationStatus;
import lombok.Data;

@Data
public class UpdateVerificationStatusDto {
    private VerificationStatus status;
    private String adminNotes;
}