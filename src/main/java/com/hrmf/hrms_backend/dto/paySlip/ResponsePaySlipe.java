package com.hrmf.hrms_backend.dto.paySlip;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class ResponsePaySlipe {
    private UUID id;
    private String image;
    private String employeeName;
    private String employeeId;
    private String jobCategory;
    private LocalDateTime transactionDate;
    private String amount;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
