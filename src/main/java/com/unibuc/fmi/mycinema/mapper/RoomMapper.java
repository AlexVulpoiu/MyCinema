package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.entity.Room;
import org.springframework.stereotype.Component;

@Component
public class RoomMapper {

    public RoomDto mapToRoomDto(Room room) {
        return RoomDto.builder()
                .name(room.getName())
                .numberOfRows(room.getNumberOfRows())
                .seatsPerRow(room.getSeatsPerRow())
                .build();
    }

    public Room mapToRoom(RoomDto roomDto) {
        return Room.builder()
                .name(roomDto.getName())
                .numberOfRows(roomDto.getNumberOfRows())
                .seatsPerRow(roomDto.getSeatsPerRow())
                .build();
    }

    public RoomDetailsDto mapToRoomDetailsDto(Room room) {
        return RoomDetailsDto.builder()
                .id(room.getId())
                .name(room.getName())
                .numberOfRows(room.getNumberOfRows())
                .seatsPerRow(room.getSeatsPerRow())
                .build();
    }
}
