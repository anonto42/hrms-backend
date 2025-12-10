package com.hrmf.hrms_backend.dto.profile;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {
    private PersonalDetailsResponse personalDetails;
    private NationalIdDetailsResponse nationalIdDetails;
    private ContactInformationResponse contactInformation;
    private List<EmergencyDetailsResponse> emergencyDetails;
    private List<EducationDetailsResponse> educationDetails;
    private List<JobDetailsResponse> jobDetails;
    private ServiceDetailsResponse serviceDetails;
    private VisaPassportResponse visaPassportDetails;
    private List<TrainingDetailsResponse> trainingDetails;
    private CompanyInformationResponse companyInformation;
    private DocumentVerificationResponse documentVerification;
}