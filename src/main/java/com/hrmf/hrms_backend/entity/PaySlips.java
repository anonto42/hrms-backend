package com.hrmf.hrms_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "pay_slips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaySlips {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @Column(name = "employee_name")
    private String employeeName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_user", nullable = false)
    private User employee;

    @Column(name = "job_category")
    private String jobCategory;

    @Column(name = "transaction_date")
    private LocalDate transactionDate;

    private String amount;

    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
