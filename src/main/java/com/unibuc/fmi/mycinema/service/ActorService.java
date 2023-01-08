package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.entity.Actor;

import java.util.List;

public interface ActorService {

    List<Actor> getActors();

    ActorDto addActor(ActorDto actorDto);

    ActorDto editActor(Long id, ActorDto actorDto);

    ActorDto deleteActor(Long id);
}
