package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.dto.OrderDto;
import com.unibuc.fmi.mycinema.entity.Order;
import com.unibuc.fmi.mycinema.entity.Ticket;

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
                .movieDate(LocalDate.of(2023, 4, 22))
                .movieHour(LocalTime.of(20, 0, 30))
                .build();
    }

    public static OrderDetailsDto mockOrderDetailsDto() {
        return OrderDetailsDto.builder()
                .movieName("Test movie")
                .roomName("Test room")
                .movieDate(LocalDate.of(2023, 4, 22))
                .movieHour(LocalTime.of(20, 0, 30))
                .purchaseDate(LocalDate.now())
                .purchaseTime(LocalTime.now())
                .totalPrice(20)
                .tickets(List.of(TicketMocks.mockTicketDto()))
                .build();
    }

    public static Order mockOrder(Integer totalPrice, List<Ticket> tickets) {
        return Order.builder()
                .date(LocalDate.now())
                .hour(LocalTime.now())
                .totalPrice(totalPrice)
                .tickets(tickets)
                .build();
    }
}
