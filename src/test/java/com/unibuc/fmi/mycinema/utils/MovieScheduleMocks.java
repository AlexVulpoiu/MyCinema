package com.unibuc.fmi.mycinema.utils;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.entity.Movie;
import com.unibuc.fmi.mycinema.entity.MovieSchedule;
import com.unibuc.fmi.mycinema.entity.Room;

import java.time.LocalDate;
import java.time.LocalTime;

public class MovieScheduleMocks {

    public static MovieScheduleDto mockMovieScheduleDto() {
        return MovieScheduleDto.builder()
                .movieName("Test movie")
                .roomName("Test room")
                .date(LocalDate.of(2023, 4, 22))
                .hour(LocalTime.of(20, 0, 30))
                .price(18)
                .build();
    }

    public static MovieSchedule mockMovieSchedule(Movie movie, Room room) {
        return MovieSchedule.builder()
                .id(mockMovieScheduleId())
                .movie(movie)
                .room(room)
                .price(18)
                .build();
    }

    public static MovieScheduleId mockMovieScheduleId() {
        return MovieScheduleId.builder()
                .movieId(1L)
                .roomId(1L)
                .date(LocalDate.of(2023, 4, 22))
                .hour(LocalTime.of(20, 0, 30))
                .build();
    }
}
