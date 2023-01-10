package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.TicketDto;
import com.unibuc.fmi.mycinema.entity.MovieSchedule;
import com.unibuc.fmi.mycinema.entity.Ticket;

public class TicketMocks {

    public static TicketDto mockTicketDto() {
        return TicketDto.builder()
                .row(2)
                .seat(1)
                .build();
    }

    public static Ticket mockTicket(MovieSchedule movieSchedule) {
        return Ticket.builder()
                .numberOfRow(1)
                .seat(1)
                .movieSchedule(movieSchedule)
                .build();
    }
}
