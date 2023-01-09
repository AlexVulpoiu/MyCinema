package com.unibuc.fmi.mycinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.service.impl.CustomerServiceImpl;
import com.unibuc.fmi.mycinema.utils.CustomerMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CustomerController.class)
public class CustomerControllerTest {

    @MockBean
    private final CustomerServiceImpl customerService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public CustomerControllerTest(CustomerServiceImpl customerService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.customerService = customerService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void addCustomerTest() throws Exception {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        when(customerService.addCustomer(any())).thenReturn(customerDto);

        String customerDtoBody = objectMapper.writeValueAsString(customerDto);
        MvcResult result = mockMvc.perform(post("/customers")
                .content(customerDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(customerDto.getName()))
                .andExpect(jsonPath("$.email").value(customerDto.getEmail()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), customerDtoBody);
    }

    @Test
    public void addCustomerThrowsUniqueConstraintExceptionTest() throws Exception {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        when(customerService.addCustomer(any())).thenThrow(new UniqueConstraintException("There is already a customer with the same email address!"));

        String customerDtoBody = objectMapper.writeValueAsString(customerDto);
        mockMvc.perform(post("/customers")
                        .content(customerDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void searchCustomersTest() throws Exception {
        CustomerDto customerDto = CustomerMocks.mockCustomerDto();
        when(customerService.searchCustomers(any())).thenReturn(List.of(customerDto));

        String customerDtoBody = objectMapper.writeValueAsString(List.of(customerDto));
        MvcResult result = mockMvc.perform(get("/customers").requestAttr("searchParam", "test"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), customerDtoBody);
    }

    @Test
    public void searchCustomersThrowsEntityNotFoundExceptionTest() throws Exception {
        when(customerService.searchCustomers(any())).thenThrow(new EntityNotFoundException("There are no customers whose name or email contains test!"));
        mockMvc.perform(get("/customers").requestAttr("searchParam", "test"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
