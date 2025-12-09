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
public class VisaPassportResponse {
    private List<IdentityDocumentResponse> passports;
    private List<IdentityDocumentResponse> visas;
}