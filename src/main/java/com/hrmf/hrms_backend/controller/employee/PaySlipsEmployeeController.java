package com.hrmf.hrms_backend.controller.employee;

import com.hrmf.hrms_backend.dto.paySlip.PaginationResponse;
import com.hrmf.hrms_backend.dto.paySlip.ResponsePaySlipe;
import com.hrmf.hrms_backend.service.PaySlipsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/employee/pay-slips")
@RequiredArgsConstructor
public class PaySlipsEmployeeController {

    private final PaySlipsService paySlipsService;

    @GetMapping
    public ResponseEntity<PaginationResponse<ResponsePaySlipe>> getEmployeePaySlipsPaginated(
            @RequestParam(required = false) String monthYear,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "transactionDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDirection
    ) {
        PaginationResponse<ResponsePaySlipe> response = paySlipsService.getEmployeePaySlips(
                monthYear, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponsePaySlipe> getPaySlipById(@PathVariable String id) {
        ResponsePaySlipe paySlip = paySlipsService.getPaySlipById(id);
        return ResponseEntity.ok(paySlip);
    }
}