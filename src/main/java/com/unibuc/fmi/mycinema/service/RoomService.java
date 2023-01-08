package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.RoomDto;

public interface RoomService {

    RoomDto addRoom(RoomDto roomDto);

    RoomDto editRoom(Long id, RoomDto roomDto);
}
