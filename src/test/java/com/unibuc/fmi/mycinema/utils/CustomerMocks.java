package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.entity.Customer;

public class CustomerMocks {

    public static CustomerDto mockCustomerDto() {
        return CustomerDto.builder()
                .name("Test customer")
                .email("test@gmail.com")
                .build();
    }

    public static Customer mockCustomer() {
        return Customer.builder()
                .id(1L)
                .name("Test customer")
                .email("test@gmail.com")
                .build();
    }
}
