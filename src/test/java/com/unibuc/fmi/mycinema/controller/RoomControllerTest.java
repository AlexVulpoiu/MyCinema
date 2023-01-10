package com.unibuc.fmi.mycinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.service.impl.RoomServiceImpl;
import com.unibuc.fmi.mycinema.utils.RoomMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoomController.class)
public class RoomControllerTest {

    @MockBean
    private final RoomServiceImpl roomService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public RoomControllerTest(RoomServiceImpl roomService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.roomService = roomService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getRoomsTest() throws Exception {
        List<RoomDetailsDto> rooms = new ArrayList<>();
        for(long i = 1; i <= 5; i++) {
            rooms.add(RoomMocks.mockRoomDetailsDto(i));
        }
        when(roomService.getRooms()).thenReturn(rooms);

        String roomsBody = objectMapper.writeValueAsString(rooms);
        MvcResult result = mockMvc.perform(get("/rooms"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), roomsBody);
    }

    @Test
    public void addRoomTest() throws Exception {
        RoomDto roomDto = RoomMocks.mockRoomDto();
        when(roomService.add(any())).thenReturn(roomDto);

        String roomDtoBody = objectMapper.writeValueAsString(roomDto);
        MvcResult result = mockMvc.perform(post("/rooms")
                        .content(roomDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.numberOfRows").value(12))
                .andExpect(jsonPath("$.seatsPerRow").value(10))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), roomDtoBody);
    }

    @Test
    public void addRoomThrowsConflictExceptionTest() throws Exception {
        RoomDto roomDto = RoomMocks.mockRoomDto();
        when(roomService.add(any())).thenThrow(new UniqueConstraintException("There is already a cinema room with the same name!"));

        String roomDtoBody = objectMapper.writeValueAsString(roomDto);
        mockMvc.perform(post("/rooms")
                        .content(roomDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void editRoomTest() throws Exception {
        RoomDto roomDto = RoomMocks.mockRoomDto();
        when(roomService.editRoom(any(), any())).thenReturn(roomDto);

        String roomDtoBody = objectMapper.writeValueAsString(roomDto);
        MvcResult result = mockMvc.perform(put("/rooms/1")
                        .content(roomDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test name"))
                .andExpect(jsonPath("$.numberOfRows").value(12))
                .andExpect(jsonPath("$.seatsPerRow").value(10))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), roomDtoBody);
    }

    @Test
    public void editRoomThrowsNotFoundExceptionTest() throws Exception {
        RoomDto roomDto = RoomMocks.mockRoomDto();
        when(roomService.editRoom(any(), any())).thenThrow(new EntityNotFoundException("There is no room with id: 1!"));

        String roomDtoBody = objectMapper.writeValueAsString(roomDto);
        mockMvc.perform(put("/rooms/1")
                        .content(roomDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void editRoomThrowsConflictExceptionTest() throws Exception {
        RoomDto roomDto = RoomMocks.mockRoomDto();
        when(roomService.editRoom(any(), any())).thenThrow(new UniqueConstraintException("There is already a cinema room with the same name!"));

        String roomDtoBody = objectMapper.writeValueAsString(roomDto);
        mockMvc.perform(put("/rooms/1")
                        .content(roomDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }
}
