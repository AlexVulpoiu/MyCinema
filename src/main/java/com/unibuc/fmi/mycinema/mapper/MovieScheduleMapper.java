package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.entity.MovieSchedule;
import org.springframework.stereotype.Component;

@Component
public class MovieScheduleMapper {

    public MovieScheduleDto mapToMovieScheduleDto(MovieSchedule movieSchedule) {
        return MovieScheduleDto.builder()
                .movieName(movieSchedule.getMovie().getName())
                .roomName(movieSchedule.getRoom().getName())
                .date(movieSchedule.getId().getDate())
                .hour(movieSchedule.getId().getHour())
                .price(movieSchedule.getPrice())
                .build();
    }
}
