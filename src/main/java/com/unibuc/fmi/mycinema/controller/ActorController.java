package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.service.impl.ActorServiceImpl;
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
@RequestMapping("/actors")
@Tag(name = "Actors management", description = "Manage actors from database")
public class ActorController {

    private final ActorServiceImpl actorService;

    @Autowired
    public ActorController(ActorServiceImpl actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    @Operation(summary = "Get all actors", description = "Get information about all actors in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "The actors list.")
    })
    public ResponseEntity<List<ActorDetailsDto>> getActors() {
        return ResponseEntity.ok(actorService.getActors());
    }

    @PostMapping
    @Operation(summary = "Add an actor", description = "Add an actor in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the actor"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 actors with the same name")
    })
    public ResponseEntity<ActorDto> addActor(@Parameter(description = "actor details") @Valid @RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.add(actorDto));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Edit an actor", description = "Edit details of an actor from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edit the actor"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There is no actor having the provided id"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 actors with the same name")
    })
    public ResponseEntity<ActorDto> editActor(@Parameter(description = "the id of the actor that will be edited") @PathVariable Long id,
                                              @Parameter(description = "the new details of the actor") @Valid @RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.editActor(id, actorDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an actor", description = "Delete an actor from database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully edit the actor"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The actor has roles in some movies and can't be deleted"),
            @ApiResponse(responseCode = "404", description = "Not found exception. There is no actor having the provided id")
    })
    public ResponseEntity<ActorDto> deleteActor(@Parameter(description = "the id of the actor that will be deleted") @PathVariable Long id) {
        return ResponseEntity.ok(actorService.deleteActor(id));
    }
}
