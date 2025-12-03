package com.hrmf.user_service.dto;

import com.hrmf.user_service.entity.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {
    private Address.AddressType addressType;
    private String postCode;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String country;
    private Boolean isPrimary;
}