package com.hrmf.hrms_backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "contact_information")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContactInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String postCode;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String country;
    private String mobile;

    @Column(name = "emergency_contact")
    private String emergencyContact;
    private String email;
}
