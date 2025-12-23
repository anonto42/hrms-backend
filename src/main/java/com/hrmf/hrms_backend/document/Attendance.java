package com.hrmf.hrms_backend.document;

import com.hrmf.hrms_backend.enums.AttendanceApproval;
import com.hrmf.hrms_backend.enums.AttendanceStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "attendances")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Attendance {
    @Id
    private String id;

    private String userId;
    private String employerId;
    private String employeeName;
    private String employeeEmail;

    private LocalDate attendanceDate;
    private AttendanceStatus status;

    // Work timings
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private Long totalWorkMinutes;

    // Break management
    @Builder.Default
    private List<BreakRecord> breaks = new ArrayList<>();
    private Long totalBreakMinutes;

    // Pause management
    @Builder.Default
    private List<PauseRecord> pauses = new ArrayList<>();
    private Long totalPauseMinutes;

    // Notes
    private String checkOutNote;
    private String adminNote;

    // Approval
    private AttendanceApproval approvalStatus;
    private String approvedBy;
    private LocalDateTime approvedAt;
    private String rejectionReason;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    // Getter
    public ArrayList<BreakRecord> getBreaks() {
        return breaks != null ? new ArrayList<>(breaks) : null;
    }

    // Setter
    public void setBreaks(List<BreakRecord> breaks) {
        this.breaks = breaks != null ? new ArrayList<BreakRecord>(breaks) : null;
    }

    // Helper methods
    public boolean isCheckedIn() {
        return checkInTime != null && checkOutTime == null;
    }

    public boolean isOnBreak() {
        return breaks.stream().anyMatch(BreakRecord::isActive);
    }

    public boolean isPaused() {
        return pauses.stream().anyMatch(PauseRecord::isActive);
    }
}