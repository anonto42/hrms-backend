package com.hrmf.hrms_backend.controller.employee;

import com.hrmf.hrms_backend.dto.paySlip.ResponsePaySlipe;
import com.hrmf.hrms_backend.service.PaySlipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee/pay-slips")
@RequiredArgsConstructor
public class PaySlipsEmployeeController {

    private final PaySlipsService paySlipsService;

    @GetMapping
    public ResponseEntity<List<ResponsePaySlipe>> getEmployeePaySlips(
            @RequestParam(required = false) String monthYear
    ) {
        List<ResponsePaySlipe> paySlips = paySlipsService.getEmployeePaySlips(monthYear);
        return ResponseEntity.ok(paySlips);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePaySlipe> getPaySlipById(@PathVariable UUID id) {
        ResponsePaySlipe paySlip = paySlipsService.getPaySlipById(id);
        return ResponseEntity.ok(paySlip);
    }
}