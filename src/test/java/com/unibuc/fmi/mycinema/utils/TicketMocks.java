package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.TicketDto;

public class TicketMocks {

    public static TicketDto mockTicketDto() {
        return TicketDto.builder()
                .row(3)
                .seat(4)
                .build();
    }
}
