package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.OrderDto;
import com.unibuc.fmi.mycinema.service.impl.OrderServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<?> addOrder(@Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.addOrder(orderDto));
    }
}
