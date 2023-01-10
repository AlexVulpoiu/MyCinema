package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.entity.Room;

import java.util.ArrayList;

public class RoomMocks {

    public static RoomDto mockRoomDto() {
        return RoomDto.builder()
                .name("Test room")
                .numberOfRows(12)
                .seatsPerRow(10)
                .build();
    }

    public static RoomDetailsDto mockRoomDetailsDto() {
        return RoomDetailsDto.builder()
                .id(1L)
                .name("Test room")
                .numberOfRows(12)
                .seatsPerRow(10)
                .build();
    }

    public static RoomDetailsDto mockRoomDetailsDto(Long id) {
        return RoomDetailsDto.builder()
                .id(id)
                .name("Test room " + id)
                .numberOfRows(id.intValue() * 5)
                .seatsPerRow(id.intValue() * 10)
                .build();
    }

    public static Room mockRoom() {
        return Room.builder()
                .id(1L)
                .name("Test room")
                .numberOfRows(12)
                .seatsPerRow(10)
                .schedules(new ArrayList<>())
                .build();
    }
}
