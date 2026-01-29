package com.example.engine.service;

import com.example.engine.domain.entity.Customer;
import com.example.engine.dto.CreateCustomerRequest;
import com.example.engine.dto.CustomerDto;
import com.example.engine.mapper.CustomerMapper;
import com.example.engine.repository.CustomerRepository;
import com.example.engine.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public ApiResponse<CustomerDto> createCustomer(CreateCustomerRequest request) {
        try {
            log.info("Creating customer");

            Customer customer = customerMapper.toEntity(request);
            Customer savedCustomer = customerRepository.save(customer);

            return ApiResponse.<CustomerDto>builder()
                    .success(true)
                    .message("Customer created successfully")
                    .data(customerMapper.toDto(savedCustomer))
                    .status(HttpStatus.CREATED)
                    .build();

        } catch (Exception ex) {
            log.error("Error while creating customer", ex);

            return ApiResponse.<CustomerDto>builder()
                    .success(false)
                    .message("Failed to create customer")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<CustomerDto> getCustomerById(Long customerId) {
        try {
            log.info("Fetching customer. customerId={}", customerId);

            Customer customer = customerRepository.findById(customerId)
                    .orElse(null);

            if (customer == null) {
                log.warn("Customer not found. customerId={}", customerId);

                return ApiResponse.<CustomerDto>builder()
                        .success(false)
                        .message("Customer not found")
                        .status(HttpStatus.NOT_FOUND)
                        .build();
            }

            return ApiResponse.<CustomerDto>builder()
                    .success(true)
                    .message("Customer fetched successfully")
                    .data(customerMapper.toDto(customer))
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching customer", ex);

            return ApiResponse.<CustomerDto>builder()
                    .success(false)
                    .message("Failed to fetch customer")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }

    public ApiResponse<List<CustomerDto>> getAllCustomers() {
        try {
            log.info("Fetching all customers");

            List<CustomerDto> customers = customerRepository.findAll()
                    .stream()
                    .map(customerMapper::toDto)
                    .toList();

            return ApiResponse.<List<CustomerDto>>builder()
                    .success(true)
                    .message("Customers fetched successfully")
                    .data(customers)
                    .status(HttpStatus.OK)
                    .build();

        } catch (Exception ex) {
            log.error("Error while fetching customers", ex);

            return ApiResponse.<List<CustomerDto>>builder()
                    .success(false)
                    .message("Failed to fetch customers")
                    .error(ex.getMessage())
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
        }
    }
}
