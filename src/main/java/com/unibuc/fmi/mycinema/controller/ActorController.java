package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.service.impl.ActorServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actors")
public class ActorController {

    private final ActorServiceImpl actorService;

    @Autowired
    public ActorController(ActorServiceImpl actorService) {
        this.actorService = actorService;
    }

    @GetMapping
    public ResponseEntity<?> getActors() {
        return ResponseEntity.ok(actorService.getActors());
    }

    @PostMapping
    public ResponseEntity<?> addActor(@Valid @RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.addActor(actorDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editActor(@PathVariable Long id, @Valid @RequestBody ActorDto actorDto) {
        return ResponseEntity.ok(actorService.editActor(id, actorDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteActor(@PathVariable Long id) {
        return ResponseEntity.ok(actorService.deleteActor(id));
    }
}
