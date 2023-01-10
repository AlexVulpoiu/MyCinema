package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;
import com.unibuc.fmi.mycinema.service.impl.OrderServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@Tag(name = "Orders management", description = "Manage orders")
public class OrderController {

    private final OrderServiceImpl orderService;

    @Autowired
    public OrderController(OrderServiceImpl orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @Operation(summary = "Add an order", description = "Order tickets for a movie")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully place the order"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The movie is not scheduled at requested date and time"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The order can't be made for a past date"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. There are not enough tickets available"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The customer doesn't exist"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The movie doesn't exist"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The cinema room doesn't exist")
    })
    public ResponseEntity<OrderDetailsDto> addOrder(@Parameter(description = "details about customer, movie, cinema room, date and time, number of tickets") @Valid @RequestBody OrderDto orderDto) {
        return ResponseEntity.ok(orderService.add(orderDto));
    }
}
