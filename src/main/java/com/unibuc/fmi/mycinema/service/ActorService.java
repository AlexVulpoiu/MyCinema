package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;

import java.util.List;

public interface ActorService {

    List<ActorDetailsDto> getActors();

    ActorDto addActor(ActorDto actorDto);

    ActorDto editActor(Long id, ActorDto actorDto);

    ActorDto deleteActor(Long id);
}
