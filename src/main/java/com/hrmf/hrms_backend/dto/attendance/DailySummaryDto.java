package com.hrmf.hrms_backend.dto.attendance;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySummaryDto {
    private LocalDate date;
    private String status;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Long workHours;
    private Long breakMinutes;
    private Long pauseMinutes;
}
