package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.CustomerDto;

import java.util.List;

public interface CustomerService {

    List<CustomerDto> searchCustomers(String searchParam);
}
