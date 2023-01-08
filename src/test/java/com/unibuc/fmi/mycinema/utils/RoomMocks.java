package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.RoomDto;

public class RoomMocks {

    public static RoomDto mockRoomDto() {
        return RoomDto.builder()
                .name("Test name")
                .numberOfRows(12)
                .seatsPerRow(10)
                .build();
    }
}
