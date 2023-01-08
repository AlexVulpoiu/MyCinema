package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;

import java.util.List;

public interface RoomService {

    List<RoomDetailsDto> getRooms();

    RoomDto addRoom(RoomDto roomDto);

    RoomDto editRoom(Long id, RoomDto roomDto);
}
