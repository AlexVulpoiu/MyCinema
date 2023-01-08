package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.entity.Actor;

public class ActorMocks {

    public static ActorDto mockActorDto() {
        return ActorDto.builder()
                .name("Test name")
                .build();
    }

    public static Actor mockActor(Long id) {
        return Actor.builder()
                .id(id)
                .name("Test" + " name".repeat(id.intValue()))
                .build();
    }
}
