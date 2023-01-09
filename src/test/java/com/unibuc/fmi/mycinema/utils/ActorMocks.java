package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;

public class ActorMocks {

    public static ActorDto mockActorDto() {
        return ActorDto.builder()
                .name("Test name")
                .build();
    }

    public static ActorDetailsDto mockActorDetailsDto(Long id) {
        return ActorDetailsDto.builder()
                .id(id)
                .name("Test" + " name".repeat(id.intValue()))
                .build();
    }
}
