package com.unibuc.fmi.mycinema.service.impl;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.entity.Room;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.RoomMapper;
import com.unibuc.fmi.mycinema.repository.RoomRepository;
import com.unibuc.fmi.mycinema.service.CommonService;
import com.unibuc.fmi.mycinema.service.RoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RoomServiceImpl implements RoomService, CommonService<RoomDto, RoomDto> {

    private final RoomRepository roomRepository;

    private final RoomMapper roomMapper;

    @Autowired
    public RoomServiceImpl(RoomRepository roomRepository, RoomMapper roomMapper) {
        this.roomRepository = roomRepository;
        this.roomMapper = roomMapper;
    }

    @Override
    public List<RoomDetailsDto> getRooms() {
        return roomRepository.findAll().stream().map(roomMapper::mapToRoomDetailsDto).collect(Collectors.toList());
    }

    @Override
    public RoomDto add(RoomDto roomDto) {
        Optional<Room> optionalRoom = roomRepository.findByName(roomDto.getName());
        if(optionalRoom.isPresent()) {
            throw new UniqueConstraintException("There is already a cinema room with the same name!");
        }
        return roomMapper.mapToRoomDto(roomRepository.save(roomMapper.mapToRoom(roomDto)));
    }

    @Override
    public RoomDto editRoom(Long id, RoomDto roomDto) {
        Optional<Room> optionalRoom = roomRepository.findById(id);
        if(optionalRoom.isEmpty()) {
            throw new EntityNotFoundException("There is no room with id: " + id + "!");
        }

        Optional<Room> optionalRoomByName = roomRepository.findByName(roomDto.getName());
        if(optionalRoomByName.isPresent() && !Objects.equals(optionalRoomByName.get().getId(), id)) {
            throw new UniqueConstraintException("There is already a cinema room with the same name!");
        }

        Room room = optionalRoom.get();
        room.setName(roomDto.getName());
        room.setNumberOfRows(roomDto.getNumberOfRows());
        room.setSeatsPerRow(roomDto.getSeatsPerRow());

        return roomMapper.mapToRoomDto(roomRepository.save(room));
    }
}
