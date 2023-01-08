package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.service.impl.RoomServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rooms")
public class RoomController {

    private final RoomServiceImpl roomService;

    @Autowired
    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @PostMapping
    public ResponseEntity<?> addRoom(@Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.addRoom(roomDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editRoom(@PathVariable Long id, @Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.editRoom(id, roomDto));
    }
}
