package com.unibuc.fmi.mycinema.service.impl;

import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.entity.Customer;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.CustomerMapper;
import com.unibuc.fmi.mycinema.repository.CustomerRepository;
import com.unibuc.fmi.mycinema.service.CommonService;
import com.unibuc.fmi.mycinema.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerServiceImpl implements CustomerService, CommonService<CustomerDto, CustomerDto> {

    private final CustomerRepository customerRepository;

    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    public CustomerDto add(CustomerDto customerDto) {
        Optional<Customer> optionalCustomer = customerRepository.findByEmail(customerDto.getEmail());
        if(optionalCustomer.isPresent()) {
            throw new UniqueConstraintException("There is already a customer with the same email address!");
        }
        return customerMapper.mapToCustomerDto(customerRepository.save(customerMapper.mapToCustomer(customerDto)));
    }

    @Override
    public List<CustomerDto> searchCustomers(String searchParam) {
        if(searchParam == null) {
            return customerRepository.findAll().stream().map(customerMapper::mapToCustomerDto).toList();
        }

        searchParam = searchParam.toLowerCase();
        List<Customer> customers = customerRepository.searchByNameOrEmail(searchParam);
        if(customers.isEmpty()) {
            throw new EntityNotFoundException("There are no customers whose name or email contains " + searchParam + "!");
        }
        return customers.stream().map(customerMapper::mapToCustomerDto).toList();
    }
}
