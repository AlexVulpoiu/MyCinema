package com.unibuc.fmi.mycinema.mapper;

import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.entity.Movie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MovieMapper {

    private final ActorMapper actorMapper;

    @Autowired
    public MovieMapper(ActorMapper actorMapper) {
        this.actorMapper = actorMapper;
    }

    public MovieDto mapToMovieDto(NewMovieDto movie, List<Actor> actors) {
        return MovieDto.builder()
                .name(movie.getName())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .genre(movie.getGenre())
                .actors(actors)
                .build();
    }

    public Movie mapToMovie(MovieDto movieDto) {
        return Movie.builder()
                .name(movieDto.getName())
                .description(movieDto.getDescription())
                .duration(movieDto.getDuration())
                .genre(movieDto.getGenre())
                .actors(movieDto.getActors())
                .build();
    }

    public MovieDetailsDto mapToMovieDetailsDto(Movie movie) {
        return MovieDetailsDto.builder()
                .name(movie.getName())
                .description(movie.getDescription())
                .duration(movie.getDuration())
                .genre(movie.getGenre())
                .actors(movie.getActors().stream().map(actorMapper::mapToActorDto).toList())
                .scheduleDetails(
                        movie.getSchedules().stream().map(movieSchedule ->
                                MovieDetailsDto.ScheduleDetails.builder()
                                        .date(movieSchedule.getId().getDate())
                                        .hour(movieSchedule.getId().getHour())
                                        .price(movieSchedule.getPrice())
                                        .roomName(movieSchedule.getRoom().getName())
                                        .build()
                        ).toList()
                )
                .build();
    }
}
