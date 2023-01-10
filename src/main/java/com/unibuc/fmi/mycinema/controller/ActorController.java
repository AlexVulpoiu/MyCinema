package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.service.impl.ActorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/actors")
public class ActorController {

    private final ActorServiceImpl actorService;

    @Autowired
    public ActorController(ActorServiceImpl actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<List<ActorDetailsDto>> getActors() {
        return ResponseEntity.ok(actorService.getActors());
    }

    @PostMapping
    public ResponseEntity<ActorDto> addActor(@Valid @RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.add(actorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ActorDto> editActor(@PathVariable Long id, @Valid @RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.editActor(id, actorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ActorDto> deleteActor(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.deleteActor(id));
    }
}
