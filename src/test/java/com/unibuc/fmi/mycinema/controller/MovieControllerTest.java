package com.unibuc.fmi.mycinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.dto.MoviesFiltersDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.service.impl.MovieServiceImpl;
import com.unibuc.fmi.mycinema.utils.MovieMocks;
import com.unibuc.fmi.mycinema.utils.MovieScheduleMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MovieController.class)
public class MovieControllerTest {

    @MockBean
    private final MovieServiceImpl movieService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public MovieControllerTest(MovieServiceImpl movieService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.movieService = movieService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void addMovieTest() throws Exception {
        NewMovieDto newMovieDto = MovieMocks.mockNewMovieDto();
        MovieDetailsDto movieDetailsDto = MovieMocks.mockMovieDetailsDto();
        when(movieService.add(newMovieDto)).thenReturn(movieDetailsDto);

        String newMovieDtoBody = objectMapper.writeValueAsString(newMovieDto);
        String movieDetailsDtoBody = objectMapper.writeValueAsString(movieDetailsDto);
        MvcResult result = mockMvc.perform(post("/movies")
                .content(newMovieDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(movieDetailsDto.getName()))
                .andExpect(jsonPath("$.description").value(movieDetailsDto.getDescription()))
                .andExpect(jsonPath("$.genre").value(movieDetailsDto.getGenre().toString()))
                .andExpect(jsonPath("$.duration").value(movieDetailsDto.getDuration()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), movieDetailsDtoBody);
    }

    @Test
    public void addMovieThrowsUniqueConstraintException() throws Exception {
        NewMovieDto newMovieDto = MovieMocks.mockNewMovieDto();
        when(movieService.add(newMovieDto)).thenThrow(new UniqueConstraintException("There is already a movie with the same name!"));

        String newMovieDtoBody = objectMapper.writeValueAsString(newMovieDto);
        mockMvc.perform(post("/movies")
                .content(newMovieDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void addMovieThrowsEntityNotFoundExceptionTest() throws Exception {
        NewMovieDto newMovieDto = MovieMocks.mockNewMovieDto();
        when(movieService.add(newMovieDto)).thenThrow(new EntityNotFoundException("There is no actor with id: 1!"));

        String newMovieDtoBody = objectMapper.writeValueAsString(newMovieDto);
        mockMvc.perform(post("/movies")
                        .content(newMovieDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void searchMoviesTest() throws Exception {
        MoviesFiltersDto moviesFiltersDto = MovieMocks.mockMoviesFiltersDto();
        MovieDetailsDto movieDetailsDto = MovieMocks.mockMovieDetailsDto();
        when(movieService.searchMovies(moviesFiltersDto.getName(), moviesFiltersDto.getDate(), moviesFiltersDto.getGenre())).thenReturn(List.of(movieDetailsDto));

        String moviesFiltersDtoBody = objectMapper.writeValueAsString(moviesFiltersDto);
        String movieDetailsDtoBody = objectMapper.writeValueAsString(List.of(movieDetailsDto));
        MvcResult result = mockMvc.perform(post("/movies/search")
                .content(moviesFiltersDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), movieDetailsDtoBody);
    }

    @Test
    public void scheduleMovieTest() throws Exception {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        when(movieService.scheduleMovie(any())).thenReturn(movieScheduleDto);

        String movieScheduleDtoBody = objectMapper.writeValueAsString(movieScheduleDto);
        MvcResult result = mockMvc.perform(post("/movies/schedule")
                .content(movieScheduleDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.movieName").value(movieScheduleDto.getMovieName()))
                .andExpect(jsonPath("$.roomName").value(movieScheduleDto.getRoomName()))
                .andExpect(jsonPath("$.date").value(movieScheduleDto.getDate().toString()))
                .andExpect(jsonPath("$.hour").value(movieScheduleDto.getHour().toString()))
                .andExpect(jsonPath("$.price").value(movieScheduleDto.getPrice()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), movieScheduleDtoBody);
    }

    @Test
    public void scheduleMovieThrowsMovieEntityNotFoundExceptionTest() throws Exception {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        when(movieService.scheduleMovie(any())).thenThrow(new EntityNotFoundException("There is no movie with name: Test movie!"));

        String movieScheduleDtoBody = objectMapper.writeValueAsString(movieScheduleDto);
        mockMvc.perform(post("/movies/schedule")
                        .content(movieScheduleDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void scheduleMovieThrowsRoomEntityNotFoundExceptionTest() throws Exception {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        when(movieService.scheduleMovie(any())).thenThrow(new EntityNotFoundException("There is no room with name: Test room!"));

        String movieScheduleDtoBody = objectMapper.writeValueAsString(movieScheduleDto);
        mockMvc.perform(post("/movies/schedule")
                        .content(movieScheduleDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void scheduleMovieThrowsMovieBadRequestExceptionTest() throws Exception {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        when(movieService.scheduleMovie(any())).thenThrow(new BadRequestException("The movie can't be scheduled in a past date!"));

        String movieScheduleDtoBody = objectMapper.writeValueAsString(movieScheduleDto);
        mockMvc.perform(post("/movies/schedule")
                        .content(movieScheduleDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void scheduleMovieThrowsRoomBadRequestExceptionTest() throws Exception {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        when(movieService.scheduleMovie(any())).thenThrow(new BadRequestException("The room is not available at this hour!"));

        String movieScheduleDtoBody = objectMapper.writeValueAsString(movieScheduleDto);
        mockMvc.perform(post("/movies/schedule")
                        .content(movieScheduleDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
