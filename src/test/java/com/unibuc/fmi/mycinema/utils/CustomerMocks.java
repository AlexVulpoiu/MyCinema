package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.CustomerDto;

public class CustomerMocks {

    public static CustomerDto mockCustomerDto() {
        return CustomerDto.builder()
                .name("Test name")
                .email("test@email.com")
                .build();
    }
}
