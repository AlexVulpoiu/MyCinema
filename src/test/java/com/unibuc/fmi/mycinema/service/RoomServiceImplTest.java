package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.entity.Room;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.RoomMapper;
import com.unibuc.fmi.mycinema.repository.RoomRepository;
import com.unibuc.fmi.mycinema.service.impl.RoomServiceImpl;
import com.unibuc.fmi.mycinema.utils.RoomMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.unibuc.fmi.mycinema.constants.Constants.ENTITY_NOT_FOUND;
import static com.unibuc.fmi.mycinema.constants.Constants.UNIQUE_CONSTRAINT;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoomServiceImplTest {

    @InjectMocks
    private RoomServiceImpl roomService;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomMapper roomMapper;

    @Test
    public void getRoomsTest() {
        RoomDetailsDto roomDetailsDto = RoomMocks.mockRoomDetailsDto();
        List<RoomDetailsDto> roomDetailsDtoList = List.of(roomDetailsDto);
        Room room = RoomMocks.mockRoom();
        List<Room> rooms = List.of(room);

        when(roomRepository.findAll()).thenReturn(rooms);
        when(roomMapper.mapToRoomDetailsDto(room)).thenReturn(roomDetailsDto);

        List<RoomDetailsDto> result = roomService.getRooms();
        assertEquals(result, roomDetailsDtoList);
    }

    @Test
    public void addRoomTest() {
        Room room = RoomMocks.mockRoom();
        RoomDto roomDto = RoomMocks.mockRoomDto();

        when(roomRepository.save(room)).thenReturn(room);
        when(roomMapper.mapToRoom(roomDto)).thenReturn(room);
        when(roomMapper.mapToRoomDto(room)).thenReturn(roomDto);
        RoomDto result = roomService.add(roomDto);

        assertEquals(result.getName(), roomDto.getName());
        assertEquals(result.getNumberOfRows(), roomDto.getNumberOfRows());
        assertEquals(result.getSeatsPerRow(), roomDto.getSeatsPerRow());
    }

    @Test
    public void addRoomThrowsUniqueConstraintExceptionTest() {
        RoomDto roomDto = RoomMocks.mockRoomDto();
        Room room = RoomMocks.mockRoom();
        when(roomRepository.findByName("Test room")).thenReturn(Optional.of(room));

        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> roomService.add(roomDto));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "room", "name"), uniqueConstraintException.getMessage());
    }

    @Test
    public void editRoomTest() {
        Room room = RoomMocks.mockRoom();
        RoomDto roomDto = RoomMocks.mockRoomDto();
        room.setName("Test room edit");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        roomDto.setName("Test room edit");
        when(roomMapper.mapToRoomDto(room)).thenReturn(roomDto);
        when(roomRepository.save(room)).thenReturn(room);
        RoomDto result = roomService.editRoom(1L, roomDto);

        assertEquals(result.getName(), roomDto.getName());
        assertEquals(result.getName(), "Test room edit");
    }

    @Test
    public void editRoomThrowsEntityNotFoundExceptionTest() {
        Room room = RoomMocks.mockRoom();
        RoomDto roomDto = RoomMocks.mockRoomDto();
        room.setName("Test room edit");

        when(roomRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> roomService.editRoom(1L, roomDto));
        assertEquals(String.format(ENTITY_NOT_FOUND, "room", "id", 1), entityNotFoundException.getMessage());
    }

    @Test
    public void editRoomThrowsUniqueConstraintExceptionTest() {
        Room room = RoomMocks.mockRoom();
        RoomDto roomDto = RoomMocks.mockRoomDto();
        roomDto.setName("Test room edit");
        room.setName("Test room edit");
        Room testRoom = RoomMocks.mockRoom();
        testRoom.setId(2L);
        testRoom.setName("Test room edit");

        when(roomRepository.findById(1L)).thenReturn(Optional.of(room));
        when(roomRepository.findByName("Test room edit")).thenReturn(Optional.of(testRoom));
        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> roomService.editRoom(1L, roomDto));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "room", "name"), uniqueConstraintException.getMessage());
    }
}
