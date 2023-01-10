package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.RoomDetailsDto;
import com.unibuc.fmi.mycinema.dto.RoomDto;
import com.unibuc.fmi.mycinema.service.impl.RoomServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
@Tag(name = "Cinema rooms management", description = "Manage cinema rooms from database")
public class RoomController {

    private final RoomServiceImpl roomService;

    @Autowired
    public RoomController(RoomServiceImpl roomService) {
        this.roomService = roomService;
    }

    @GetMapping
    @Operation(summary = "Get all cinema rooms", description = "Get information about all cinema rooms in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The cinema rooms list.")
    })
    public ResponseEntity<List<RoomDetailsDto>> getRooms() {
        return ResponseEntity.ok(roomService.getRooms());
    }

    @PostMapping
    @Operation(summary = "Add a cinema room", description = "Add a cinema room in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the cinema room"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 cinema rooms with the same name")
    })
    public ResponseEntity<RoomDto> addRoom(@Parameter(description = "cinema room details") @Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.add(roomDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit a cinema room", description = "Edit details of a cinema room from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edit the cinema room"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There is no cinema room having the provided id"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 cinema rooms with the same name")
    })
    public ResponseEntity<RoomDto> editRoom(@Parameter(description = "the id of the cinema room that will be edited") @PathVariable Long id,
                                            @Parameter(description = "the new details of the cinema room") @Valid @RequestBody RoomDto roomDto) {
        return ResponseEntity.ok(roomService.editRoom(id, roomDto));
    }
}
