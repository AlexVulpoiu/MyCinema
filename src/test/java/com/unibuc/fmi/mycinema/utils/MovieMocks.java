package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.dto.MoviesFiltersDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.enums.EGenre;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class MovieMocks {

    public static NewMovieDto mockNewMovieDto() {
        return NewMovieDto.builder()
                .name("Test movie")
                .description("Movie description")
                .genre(EGenre.DRAMA)
                .duration(80)
                .actorIds(List.of(1L))
                .build();
    }

    public static MovieDetailsDto mockMovieDetailsDto() {
        return MovieDetailsDto.builder()
                .name("Test movie")
                .description("Movie description")
                .genre(EGenre.DRAMA)
                .duration(80)
                .actors(List.of(ActorMocks.mockActorDto()))
                .scheduleDetails(List.of())
                .build();
    }

    public static MoviesFiltersDto mockMoviesFiltersDto() {
        return MoviesFiltersDto.builder()
                .name("Test movie")
                .date(LocalDate.of(2023, 5, 6))
                .genre(EGenre.ADVENTURE)
                .build();
    }

    public static MovieScheduleDto mockMovieScheduleDto() {
        return MovieScheduleDto.builder()
                .movieName("Test movie")
                .roomName("Test room")
                .date(LocalDate.of(2023, 4, 22))
                .hour(LocalTime.of(20, 0, 30))
                .price(18)
                .build();
    }
}
