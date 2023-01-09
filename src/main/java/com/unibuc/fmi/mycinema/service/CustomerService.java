package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    CustomerDto addCustomer(CustomerDto customerDto);

    List<CustomerDto> searchCustomers(String searchParam);
}
