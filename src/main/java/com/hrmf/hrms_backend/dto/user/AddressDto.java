package com.hrmf.hrms_backend.dto.user;

import com.hrmf.hrms_backend.enums.AddressType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AddressDto {
    private UUID id;
    private AddressType addressType;
    private String postCode;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String country;
    private Boolean isPrimary;
}