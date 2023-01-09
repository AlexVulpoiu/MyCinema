package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.entity.Actor;
import org.springframework.stereotype.Component;

@Component
public class ActorMapper {

    public ActorDto mapToActorDto(Actor actor) {
        return ActorDto.builder()
                .name(actor.getName())
                .build();
    }

    public Actor mapToActor(ActorDto actorDto) {
        return Actor.builder()
                .name(actorDto.getName())
                .build();
    }

    public ActorDetailsDto mapToActorDetailsDto(Actor actor) {
        return ActorDetailsDto.builder()
                .id(actor.getId())
                .name(actor.getName())
                .build();
    }
}
