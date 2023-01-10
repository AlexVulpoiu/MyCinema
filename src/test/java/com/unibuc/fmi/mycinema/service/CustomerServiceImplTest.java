package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.entity.Customer;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.CustomerMapper;
import com.unibuc.fmi.mycinema.repository.CustomerRepository;
import com.unibuc.fmi.mycinema.service.impl.CustomerServiceImpl;
import com.unibuc.fmi.mycinema.utils.CustomerMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private CustomerMapper customerMapper;

    @Test
    public void addCustomerTest() {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        Customer customer = CustomerMocks.mockCustomer();

        when(customerRepository.save(customer)).thenReturn(customer);
        when(customerMapper.mapToCustomer(customerDto)).thenReturn(customer);
        when(customerMapper.mapToCustomerDto(customer)).thenReturn(customerDto);
        CustomerDto result = customerService.add(customerDto);

        assertEquals(result.getName(), customerDto.getName());
        assertEquals(result.getEmail(), customerDto.getEmail());
    }

    @Test
    public void addCustomerThrowsUniqueConstraintExceptionTest() {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        Customer customer = CustomerMocks.mockCustomer();

        when(customerRepository.findByEmail("test@gmail.com")).thenReturn(Optional.of(customer));
        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> customerService.add(customerDto));
        assertEquals("There is already a customer with the same email address!", uniqueConstraintException.getMessage());
    }

    @Test
    public void searchCustomersWithNullParamTest() {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        List<CustomerDto> customerDtoList = List.of(customerDto);
        Customer customer = CustomerMocks.mockCustomer();
        List<Customer> customers = List.of(customer);

        when(customerRepository.findAll()).thenReturn(customers);
        when(customerMapper.mapToCustomerDto(customer)).thenReturn(customerDto);
        List<CustomerDto> result = customerService.searchCustomers(null);

        assertEquals(result, customerDtoList);
    }

    @Test
    public void searchCustomersWithParamTest() {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        List<CustomerDto> customerDtoList = List.of(customerDto);
        Customer customer = CustomerMocks.mockCustomer();
        List<Customer> customers = List.of(customer);

        when(customerMapper.mapToCustomerDto(customer)).thenReturn(customerDto);
        when(customerRepository.searchByNameOrEmail("test")).thenReturn(customers);
        List<CustomerDto> result = customerService.searchCustomers("test");

        assertEquals(result, customerDtoList);
    }

    @Test
    public void searchCustomersThrowsEntityNotFoundExceptionTest() {
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> customerService.searchCustomers("test"));
        assertEquals("There are no customers whose name or email contains test!", entityNotFoundException.getMessage());
    }
}
