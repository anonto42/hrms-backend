package com.hrmf.hrms_backend.repository;

import com.hrmf.hrms_backend.entity.PaySlips;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaySlipsRepository extends JpaRepository<PaySlips, UUID> {

    // Find by employee ID
    List<PaySlips> findByEmployeeId(String employeeId);

    // Find by employee ID and date range
    List<PaySlips> findByEmployeeIdAndTransactionDateBetween(
            String employeeId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Find by date range
    List<PaySlips> findByTransactionDateBetween(
            LocalDateTime startDate,
            LocalDateTime endDate
    );

    // Find by job category (department)
    List<PaySlips> findByJobCategoryIgnoreCase(String jobCategory);

    // Find by date range and job category
    List<PaySlips> findByTransactionDateBetweenAndJobCategoryIgnoreCase(
            LocalDateTime startDate,
            LocalDateTime endDate,
            String jobCategory
    );

    // Find by multiple employee IDs
    @Query("SELECT p FROM pay_slips p WHERE p.employeeId IN :employeeIds")
    List<PaySlips> findByEmployeeIds(@Param("employeeIds") List<String> employeeIds);

    // Alternative simpler query
    @Query("SELECT p FROM pay_slips p WHERE p.employeeId = :employeeId")
    List<PaySlips> findAllByEmployeeId(@Param("employeeId") String employeeId);
}