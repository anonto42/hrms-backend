package com.hrmf.hrms_backend.dto.subAdmin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DashboardStatsDto {
    private Long totalEmployees;
    private Long activeEmployees;
    private Long inactiveEmployees;

    private Long totalEmployers;
    private Long activeEmployers;
    private Long inactiveEmployers;

    private Long totalUsers;
    private Long totalActiveUsers;
    private Long totalInactiveUsers;
}
