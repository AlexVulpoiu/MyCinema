package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;

public class RoomMocks {

    public static RoomDto mockRoomDto() {
        return RoomDto.builder()
                .name("Test name")
                .numberOfRows(12)
                .seatsPerRow(10)
                .build();
    }

    public static RoomDetailsDto mockRoomDetailsDto(Long id) {
        return RoomDetailsDto.builder()
                .id(id)
                .name("Test name " + id)
                .numberOfRows(id.intValue() * 5)
                .seatsPerRow(id.intValue() * 10)
                .build();
    }
}
