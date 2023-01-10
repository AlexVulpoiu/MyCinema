package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;

public interface OrderService {

    OrderDetailsDto addOrder(OrderDto orderDto);
}
