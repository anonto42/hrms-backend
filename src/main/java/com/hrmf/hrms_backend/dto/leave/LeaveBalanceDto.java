package com.hrmf.hrms_backend.dto.leave;

import lombok.Data;
import java.util.UUID;

@Data
public class LeaveBalanceDto {
    private UUID employeeId;
    private String employeeName;
    private int sickLeaveBalance;
    private int annualLeaveBalance;
    private int casualLeaveBalance;
    private int maternityLeaveBalance;
    private int paternityLeaveBalance;
    private int unpaidLeaveBalance;

    // Used leaves
    private int usedSickLeave;
    private int usedAnnualLeave;
    private int usedCasualLeave;
    private int usedMaternityLeave;
    private int usedPaternityLeave;
    private int usedUnpaidLeave;
}