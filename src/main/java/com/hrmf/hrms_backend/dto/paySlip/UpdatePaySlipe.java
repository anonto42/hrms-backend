package com.hrmf.hrms_backend.dto.paySlip;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdatePaySlipe {
    private String image;
    private String employeeName;
    private String employeeId;
    private String jobCategory;
    private LocalDateTime transactionDate;
    private String amount;
    private String description;
}