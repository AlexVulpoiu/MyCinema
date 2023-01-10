package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.TicketDto;
import com.unibuc.fmi.mycinema.entity.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketDto mapToTicketDto(Ticket ticket) {
        return TicketDto.builder()
                .row(ticket.getNumberOfRow())
                .seat(ticket.getSeat())
                .build();
    }
}
