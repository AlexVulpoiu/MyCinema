package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.*;
import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.entity.Movie;
import com.unibuc.fmi.mycinema.enums.EGenre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class MovieMocks {

    public static NewMovieDto mockNewMovieDto() {
        return NewMovieDto.builder()
                .name("Test movie")
                .description("Test description")
                .genre(EGenre.DRAMA)
                .duration(80)
                .actorIds(List.of(1L))
                .build();
    }

    public static MovieDetailsDto mockMovieDetailsDto() {
        return MovieDetailsDto.builder()
                .name("Test movie")
                .description("Movie description")
                .genre(EGenre.ADVENTURE)
                .duration(80)
                .actors(List.of(ActorMocks.mockActorDto()))
                .scheduleDetails(List.of(
                        MovieDetailsDto.ScheduleDetails.builder()
                                .roomName("Test room")
                                .date(LocalDate.now().plusDays(2))
                                .hour(LocalTime.now())
                                .price(16)
                                .build()
                ))
                .build();
    }

    public static MoviesFiltersDto mockMoviesFiltersDto() {
        return MoviesFiltersDto.builder()
                .name("Test movie")
                .date(LocalDate.of(2023, 5, 6))
                .genre(EGenre.ADVENTURE)
                .build();
    }

    public static Movie mockMovie() {
        return Movie.builder()
                .id(1L)
                .name("Test movie")
                .description("Test description")
                .duration(100)
                .genre(EGenre.ADVENTURE)
                .actors(new ArrayList<>())
                .schedules(new ArrayList<>())
                .build();
    }

    public static MovieDto mockMovieDto(List<Actor> actors) {
        return MovieDto.builder()
                .name("Test movie")
                .description("Test description")
                .duration(100)
                .genre(EGenre.ADVENTURE)
                .actors(actors)
                .build();
    }
}
