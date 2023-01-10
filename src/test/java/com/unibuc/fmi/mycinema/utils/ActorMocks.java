package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.entity.Actor;

import java.util.ArrayList;

public class ActorMocks {

    public static ActorDto mockActorDto() {
        return ActorDto.builder()
                .name("Test actor")
                .build();
    }

    public static ActorDetailsDto mockActorDetailsDto(Long id) {
        return ActorDetailsDto.builder()
                .id(id)
                .name("Test" + " actor".repeat(id.intValue()))
                .build();
    }

    public static ActorDetailsDto mockActorDetailsDto() {
        return ActorDetailsDto.builder()
                .id(1L)
                .name("Test actor")
                .build();
    }

    public static Actor mockActor() {
        return Actor.builder()
                .id(1L)
                .name("Test actor")
                .movies(new ArrayList<>())
                .build();
    }
}
