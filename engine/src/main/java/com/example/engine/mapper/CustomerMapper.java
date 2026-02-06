package com.example.engine.mapper;

import com.example.engine.domain.entity.Customer;
import com.example.engine.dto.CreateCustomerRequest;
import com.example.engine.dto.CustomerDto;
import com.example.engine.dto.CustomerSensitiveDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {

    @Mapping(source = "fullName", target = "name")
    CustomerDto toDto(Customer customer);


    Customer toEntity(CreateCustomerRequest request);

    @Mapping(source = "fullName", target = "name")
    CustomerSensitiveDto toSensitiveDto(Customer customer);

}
