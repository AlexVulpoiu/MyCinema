package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.service.impl.RoomServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    @Autowired
    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    public ResponseEntity<List<RoomDetailsDto>> getRooms() {
        return ResponseEntity.ok(roomService.getRooms());
    }

    @PostMapping
    public ResponseEntity<RoomDto> addRoom(@Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.add(roomDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoomDto> editRoom(@PathVariable Long id, @Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.editRoom(id, roomDto));
    }
}
