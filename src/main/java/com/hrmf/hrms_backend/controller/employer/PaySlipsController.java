package com.hrmf.hrms_backend.controller.employer;

import com.hrmf.hrms_backend.dto.paySlip.CreatePaySlipe;
import com.hrmf.hrms_backend.dto.paySlip.ResponsePaySlipe;
import com.hrmf.hrms_backend.service.PaySlipsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/employer/pay-slips")
@RequiredArgsConstructor
public class PaySlipsController {

    private final PaySlipsService paySlipsService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createPaySlipe (
            @Valid @ModelAttribute CreatePaySlipe paySlipe
            ) {

        ResponsePaySlipe response = paySlipsService.createPaySlip(paySlipe);

        return ResponseEntity.ok(response);
    }


}