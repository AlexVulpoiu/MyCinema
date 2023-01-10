package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.service.impl.CustomerServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customers management", description = "Manage customers from database")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    @Autowired
    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    @Operation(summary = "Add customer", description = "Add a customer in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the customer"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 customers with the same email")
    })
    public ResponseEntity<CustomerDto> addCustomer(@Parameter(description = "customer details") @Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.add(customerDto));
    }

    @GetMapping
    @Operation(summary = "Search customers", description = "Search for customers in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found customers that match the provided criteria"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There are no customers whose name or email contain the value specified in search parameter")
    })
    public ResponseEntity<List<CustomerDto>> searchCustomers(@Parameter(description = "search param for name or email") @RequestParam(required = false) String searchParam) {
        return ResponseEntity.ok(customerService.searchCustomers(searchParam));
    }
}
