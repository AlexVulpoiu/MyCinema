package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class OrderMocks {

    public static OrderDto mockOrderDto() {
        return OrderDto.builder()
                .customerEmail("test@gmail.com")
                .numberOfTickets(1)
                .movieName("Test movie")
                .roomName("Test room")
                .movieDate(LocalDate.now().plusDays(1))
                .movieHour(LocalTime.now())
                .build();
    }

    public static OrderDetailsDto mockOrderDetailsDto() {
        return OrderDetailsDto.builder()
                .movieName("Test movie")
                .roomName("Test room")
                .movieDate(LocalDate.now().plusDays(1))
                .movieHour(LocalTime.now())
                .purchaseDate(LocalDate.now())
                .purchaseTime(LocalTime.now())
                .totalPrice(20)
                .tickets(List.of(TicketMocks.mockTicketDto()))
                .build();
    }
}
