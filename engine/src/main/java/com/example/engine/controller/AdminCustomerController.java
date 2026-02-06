package com.example.engine.controller;

import com.example.engine.dto.CustomerSensitiveDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/customers")
@RequiredArgsConstructor
public class AdminCustomerController {

    private final CustomerService customerService;

    @GetMapping("/{id}/sensitive")
    public ResponseEntity<ApiResponse<CustomerSensitiveDto>> getSensitiveCustomer(
            @PathVariable Long id,
            @RequestHeader("X-ADMIN-KEY") String adminKey) {

        if (!"super-admin-secret".equals(adminKey)) {
            return ResponseEntity.status(403).build();
        }

        ApiResponse<CustomerSensitiveDto> response =
                customerService.getSensitiveCustomerById(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
