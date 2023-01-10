package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.enums.EGenre;

import java.time.LocalDate;
import java.util.List;

public interface MovieService {

    List<MovieDetailsDto> searchMovies(String name, LocalDate date, EGenre genre);

    MovieScheduleDto scheduleMovie(MovieScheduleDto movieScheduleDto);
}
