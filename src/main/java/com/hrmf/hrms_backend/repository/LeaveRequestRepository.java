package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.LeaveRequest;
import com.hrmf.hrms_backend.enums.LeaveStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface LeaveRequestRepository extends JpaRepository<LeaveRequest, UUID> {

    Page<LeaveRequest> findByEmployeeId(UUID employeeId, Pageable pageable);

    Page<LeaveRequest> findByEmployeeIdAndStatus(UUID employeeId, LeaveStatus status, Pageable pageable);

    List<LeaveRequest> findByEmployeeIdAndStartDateBetween(
            UUID employeeId, LocalDate start, LocalDate end);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.employer.id = :employerId")
    Page<LeaveRequest> findByEmployerId(@Param("employerId") UUID employerId, Pageable pageable);

    Page<LeaveRequest> findByStatus(LeaveStatus status, Pageable pageable);

    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE " +
            "l.employee.id = :employeeId AND " +
            "l.status = 'APPROVED' AND " +
            "l.startDate <= :date AND l.endDate >= :date")
    int countApprovedLeavesOnDate(@Param("employeeId") UUID employeeId,
                                  @Param("date") LocalDate date);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.id = :employeeId " +
            "AND EXTRACT(YEAR FROM l.startDate) = :year " +
            "AND EXTRACT(MONTH FROM l.startDate) = :month")
    List<LeaveRequest> findByEmployeeIdAndYearMonth(
            @Param("employeeId") UUID employeeId,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.id = :employeeId " +
            "AND l.status = :status " +
            "AND EXTRACT(YEAR FROM l.startDate) = :year " +
            "AND EXTRACT(MONTH FROM l.startDate) = :month")
    List<LeaveRequest> findByEmployeeIdAndStatusAndYearMonth(
            @Param("employeeId") UUID employeeId,
            @Param("status") LeaveStatus status,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT COUNT(l) FROM LeaveRequest l WHERE l.employee.id = :employeeId " +
            "AND l.status = :status " +
            "AND EXTRACT(YEAR FROM l.createdAt) = :year " +
            "AND EXTRACT(MONTH FROM l.createdAt) = :month")
    Long countByEmployeeIdAndStatusAndMonthYear(
            @Param("employeeId") UUID employeeId,
            @Param("status") LeaveStatus status,
            @Param("year") int year,
            @Param("month") int month);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.employer.id = :employerId " +
            "AND l.status = :status " +
            "AND EXTRACT(YEAR FROM l.startDate) = :year " +
            "AND EXTRACT(MONTH FROM l.startDate) = :month")
    Page<LeaveRequest> findByEmployerIdAndStatusAndYearMonth(
            @Param("employerId") UUID employerId,
            @Param("status") LeaveStatus status,
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable);

    @Query("SELECT l FROM LeaveRequest l WHERE l.employee.employer.id = :employerId " +
            "AND EXTRACT(YEAR FROM l.startDate) = :year " +
            "AND EXTRACT(MONTH FROM l.startDate) = :month")
    Page<LeaveRequest> findByEmployerIdAndYearMonth(
            @Param("employerId") UUID employerId,
            @Param("year") int year,
            @Param("month") int month,
            Pageable pageable);

    // Count by type for an employee in a specific month
    @Query("SELECT l.leaveType, COUNT(l) FROM LeaveRequest l " +
            "WHERE l.employee.id = :employeeId " +
            "AND EXTRACT(YEAR FROM l.createdAt) = :year " +
            "AND EXTRACT(MONTH FROM l.createdAt) = :month " +
            "GROUP BY l.leaveType")
    List<Object[]> countByEmployeeIdAndLeaveTypeAndMonthYear(
            @Param("employeeId") UUID employeeId,
            @Param("year") int year,
            @Param("month") int month);
}