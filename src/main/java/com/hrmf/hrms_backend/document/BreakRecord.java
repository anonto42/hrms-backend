package com.hrmf.hrms_backend.document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BreakRecord {
    private String breakId;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long durationMinutes;
    private Boolean isActive;

    public boolean isActive() {
        return Boolean.TRUE.equals(isActive);
    }
}