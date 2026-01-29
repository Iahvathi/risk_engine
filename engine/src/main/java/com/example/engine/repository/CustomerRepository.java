package com.example.engine.repository;

import com.example.engine.domain.entity.Customer;
import com.example.engine.domain.enums.CustomerStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByNationalId(String nationalId); //prevents duplicate customers
    List<Customer> findByStatus(CustomerStatus status);

}
