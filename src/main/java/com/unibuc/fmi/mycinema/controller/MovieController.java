package com.unibuc.fmi.mycinema.controller;

import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.dto.MoviesFiltersDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.service.impl.MovieServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/movies")
@Tag(name = "Movies management", description = "Manage movies from database")
public class MovieController {

    private final MovieServiceImpl movieService;

    @Autowired
    public MovieController(MovieServiceImpl movieService) {
        this.movieService = movieService;
    }

    @PostMapping
    @Operation(summary = "Add movie", description = "Add a movie in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully add the movie"),
            @ApiResponse(responseCode = "404", description = "Not found exception. One of the actors specified for this movie doesn't exist in database"),
            @ApiResponse(responseCode = "409", description = "Conflict exception. There can't be 2 movies with the same name")
    })
    public ResponseEntity<MovieDetailsDto> addMovie(@Valid @RequestBody NewMovieDto movieDto) {
        return ResponseEntity.ok(movieService.add(movieDto));
    }

    @PostMapping("/search")
    @Operation(summary = "Search movies", description = "Search for movies in database")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found movies that match the provided criteria"),
    })
    public ResponseEntity<List<MovieDetailsDto>> searchMovies(@Parameter(description = "filters for movie name, schedule date or genre") @RequestBody MoviesFiltersDto moviesFiltersDto) {
        return ResponseEntity.ok(movieService.searchMovies(moviesFiltersDto.getName(), moviesFiltersDto.getDate(), moviesFiltersDto.getGenre()));
    }

    @PostMapping("/schedule")
    @Operation(summary = "Schedule a movie", description = "Schedule a movie according to provided parameters")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully schedule the movie"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The movie can't be scheduled in a past date"),
            @ApiResponse(responseCode = "400", description = "Bad request exception. The room is not available at the requested time"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The movie doesn't exist"),
            @ApiResponse(responseCode = "404", description = "Not found exception. The cinema room doesn't exist")
    })
    public ResponseEntity<MovieScheduleDto> scheduleMovie(@Parameter(description = "details of the movie, the cinema room and schedule date and time") @Valid @RequestBody MovieScheduleDto movieScheduleDto) {
        return ResponseEntity.ok(movieService.scheduleMovie(movieScheduleDto));
    }
}
