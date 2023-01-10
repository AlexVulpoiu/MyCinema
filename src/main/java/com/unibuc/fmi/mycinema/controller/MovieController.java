package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.dto.MoviesFiltersDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.service.impl.MovieServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
public class MovieController {

    private final MovieServiceImpl movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    public ResponseEntity<MovieDetailsDto> addMovie(@Valid @RequestBody NewMovieDto movieDto) {
        return ResponseEntity.ok(movieService.addMovie(movieDto));
    }

    @PostMapping("/search")
    public ResponseEntity<List<MovieDetailsDto>> searchMovies(@RequestBody MoviesFiltersDto moviesFiltersDto) {
        return ResponseEntity.ok(movieService.searchMovies(moviesFiltersDto.getName(), moviesFiltersDto.getDate(), moviesFiltersDto.getGenre()));
    }

    @PostMapping("/schedule")
    public ResponseEntity<MovieScheduleDto> scheduleMovie(@Valid @RequestBody MovieScheduleDto movieScheduleDto) {
        return ResponseEntity.ok(movieService.scheduleMovie(movieScheduleDto));
    }
}
