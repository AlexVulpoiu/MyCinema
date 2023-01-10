package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.OrderDetailsDto;
import com.unibuc.fmi.mycinema.entity.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OrderMapper {

    private final TicketMapper ticketMapper;

    @Autowired
    public OrderMapper(TicketMapper ticketMapper) {
        this.ticketMapper = ticketMapper;
    }

    public OrderDetailsDto mapToOrderDetailsDto(Order order) {
        return OrderDetailsDto.builder()
                .movieName(order.getTickets().get(0).getMovieSchedule().getMovie().getName())
                .roomName(order.getTickets().get(0).getMovieSchedule().getRoom().getName())
                .movieDate(order.getTickets().get(0).getMovieSchedule().getId().getDate())
                .movieHour(order.getTickets().get(0).getMovieSchedule().getId().getHour())
                .purchaseDate(order.getDate())
                .purchaseTime(order.getHour())
                .totalPrice(order.getTotalPrice())
                .tickets(order.getTickets().stream().map(ticketMapper::mapToTicketDto).toList())
                .build();
    }
}
