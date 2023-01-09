package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.CustomerDto;
import com.unibuc.fmi.mycinema.service.impl.CustomerServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final CustomerServiceImpl customerService;

    @Autowired
    public CustomerController(CustomerServiceImpl customerService) {
        this.customerService = customerService;
    }

    @PostMapping
    public ResponseEntity<?> addCustomer(@Valid @RequestBody CustomerDto customerDto) {
        return ResponseEntity.ok(customerService.addCustomer(customerDto));
    }

    @GetMapping
    public ResponseEntity<?> searchCustomers(@RequestParam(required = false) String searchParam) {
        return ResponseEntity.ok(customerService.searchCustomers(searchParam));
    }
}
