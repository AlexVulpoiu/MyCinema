package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.entity.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    public CustomerDto mapToCustomerDto(Customer customer) {
        return CustomerDto.builder()
                .name(customer.getName())
                .email(customer.getEmail())
                .build();
    }

    public Customer mapToCustomer(CustomerDto customerDto) {
        return Customer.builder()
                .name(customerDto.getName())
                .email(customerDto.getEmail())
                .build();
    }
}
