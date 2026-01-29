package com.example.engine.controller;

import com.example.engine.dto.CreateCustomerRequest;
import com.example.engine.dto.CustomerDto;
import com.example.engine.response.ApiResponse;
import com.example.engine.service.CustomerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @PostMapping
    public ResponseEntity<ApiResponse<CustomerDto>> createCustomer(
            @Valid @RequestBody CreateCustomerRequest request) {

        ApiResponse<CustomerDto> response =
                customerService.createCustomer(request);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CustomerDto>> getCustomer(@PathVariable Long id) {

        ApiResponse<CustomerDto> response =
                customerService.getCustomerById(id);

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CustomerDto>>> getAllCustomers() {

        ApiResponse<List<CustomerDto>> response =
                customerService.getAllCustomers();

        return ResponseEntity
                .status(response.getStatus())
                .body(response);
    }
}
