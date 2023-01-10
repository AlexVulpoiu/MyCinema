package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.entity.Movie;
import com.unibuc.fmi.mycinema.entity.MovieSchedule;
import com.unibuc.fmi.mycinema.entity.Room;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.MovieMapper;
import com.unibuc.fmi.mycinema.mapper.MovieScheduleMapper;
import com.unibuc.fmi.mycinema.repository.ActorRepository;
import com.unibuc.fmi.mycinema.repository.MovieRepository;
import com.unibuc.fmi.mycinema.repository.MovieScheduleRepository;
import com.unibuc.fmi.mycinema.repository.RoomRepository;
import com.unibuc.fmi.mycinema.service.impl.MovieServiceImpl;
import com.unibuc.fmi.mycinema.utils.ActorMocks;
import com.unibuc.fmi.mycinema.utils.MovieMocks;
import com.unibuc.fmi.mycinema.utils.MovieScheduleMocks;
import com.unibuc.fmi.mycinema.utils.RoomMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @InjectMocks
    private MovieServiceImpl movieService;

    @Mock
    private MovieRepository movieRepository;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private MovieScheduleRepository movieScheduleRepository;

    @Mock
    private MovieMapper movieMapper;

    @Mock
    private MovieScheduleMapper movieScheduleMapper;

    @Test
    public void addMovieTest() {
        Movie movie = MovieMocks.mockMovie();
        NewMovieDto newMovieDto = MovieMocks.mockNewMovieDto();
        Actor actor = ActorMocks.mockActor();
        MovieDto movieDto = MovieMocks.mockMovieDto(List.of(actor));
        MovieDetailsDto movieDetailsDto = MovieMocks.mockMovieDetailsDto();

        when(movieRepository.save(movie)).thenReturn(movie);
        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        when(movieMapper.mapToMovieDto(newMovieDto, List.of(actor))).thenReturn(movieDto);
        when(movieMapper.mapToMovie(movieDto)).thenReturn(movie);
        when(movieMapper.mapToMovieDetailsDto(movie)).thenReturn(movieDetailsDto);
        MovieDetailsDto result = movieService.add(newMovieDto);

        assertEquals(result.getName(), movieDetailsDto.getName());
        assertEquals(result.getDescription(), movieDetailsDto.getDescription());
        assertEquals(result.getDuration(), movieDetailsDto.getDuration());
        assertEquals(result.getGenre(), movieDetailsDto.getGenre());
    }

    @Test
    public void addMovieThrowsUniqueConstraintExceptionTest() {
        Movie movie = MovieMocks.mockMovie();
        NewMovieDto newMovieDto = MovieMocks.mockNewMovieDto();
        when(movieRepository.findByName("Test movie")).thenReturn(Optional.of(movie));

        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> movieService.add(newMovieDto));
        assertEquals("There is already a movie with the same name!", uniqueConstraintException.getMessage());
    }

    @Test
    public void addMovieThrowsEntityNotFoundExceptionTest() {
        NewMovieDto newMovieDto = MovieMocks.mockNewMovieDto();

        when(actorRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> movieService.add(newMovieDto));
        assertEquals("There is no actor with id: 1!", entityNotFoundException.getMessage());
    }

    @Test
    public void searchMoviesTest() {
        Movie movie = MovieMocks.mockMovie();
        MovieDetailsDto movieDetailsDto = MovieMocks.mockMovieDetailsDto();

        when(movieRepository.findAll()).thenReturn(List.of(movie));
        when(movieMapper.mapToMovieDetailsDto(movie)).thenReturn(movieDetailsDto);
        List<MovieDetailsDto> result = movieService.searchMovies(movie.getName(), LocalDate.now().plusDays(2), movie.getGenre());

        assertEquals(result, List.of(movieDetailsDto));
    }

    @Test
    public void scheduleMovieInFreeRoomTest() {
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        MovieSchedule movieSchedule = MovieScheduleMocks.mockMovieSchedule(movie, room);
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();

        when(movieRepository.findByName("Test movie")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("Test room")).thenReturn(Optional.of(room));
        when(movieScheduleRepository.save(movieSchedule)).thenReturn(movieSchedule);
        when(movieScheduleMapper.mapToMovieScheduleDto(movieSchedule)).thenReturn(movieScheduleDto);
        MovieScheduleDto result = movieService.scheduleMovie(movieScheduleDto);

        assertEquals(result, movieScheduleDto);
    }

    @Test
    public void scheduleMovieInRoomWithOtherMoviesTest() {
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        MovieSchedule movieSchedule = MovieScheduleMocks.mockMovieSchedule(movie, room);
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();

        MovieSchedule pastMovieSchedule = MovieScheduleMocks.mockMovieSchedule(movie, room);
        MovieScheduleId movieScheduleId = MovieScheduleMocks.mockMovieScheduleId();
        movieScheduleId.setDate(LocalDate.now().minusDays(2));
        pastMovieSchedule.setId(movieScheduleId);
        room.setSchedules(List.of(pastMovieSchedule));

        when(movieRepository.findByName("Test movie")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("Test room")).thenReturn(Optional.of(room));
        when(movieScheduleRepository.save(movieSchedule)).thenReturn(movieSchedule);
        when(movieScheduleMapper.mapToMovieScheduleDto(movieSchedule)).thenReturn(movieScheduleDto);
        MovieScheduleDto result = movieService.scheduleMovie(movieScheduleDto);

        assertEquals(result, movieScheduleDto);
    }

    @Test
    public void scheduleMovieThrowsMovieEntityNotFoundExceptionTest() {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();

        when(movieRepository.findByName("Test movie")).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> movieService.scheduleMovie(movieScheduleDto));
        assertEquals("There is no movie with name: Test movie!", entityNotFoundException.getMessage());
    }

    @Test
    public void scheduleMovieThrowsRoomEntityNotFoundExceptionTest() {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        Movie movie = MovieMocks.mockMovie();

        when(movieRepository.findByName("Test movie")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("Test room")).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> movieService.scheduleMovie(movieScheduleDto));
        assertEquals("There is no room with name: Test room!", entityNotFoundException.getMessage());
    }

    @Test
    public void scheduleMovieThrowsPastDateBadRequestExceptionTest() {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        movieScheduleDto.setDate(LocalDate.now().minusDays(2));
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();

        when(movieRepository.findByName("Test movie")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("Test room")).thenReturn(Optional.of(room));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> movieService.scheduleMovie(movieScheduleDto));
        assertEquals("The movie can't be scheduled in a past date!", badRequestException.getMessage());
    }

    @Test
    public void scheduleMovieThrowsRoomNotAvailableBadRequestExceptionTest() {
        MovieScheduleDto movieScheduleDto = MovieScheduleMocks.mockMovieScheduleDto();
        Movie movie = MovieMocks.mockMovie();
        Room room = RoomMocks.mockRoom();
        room.setSchedules(List.of(MovieScheduleMocks.mockMovieSchedule(movie, room)));

        when(movieRepository.findByName("Test movie")).thenReturn(Optional.of(movie));
        when(roomRepository.findByName("Test room")).thenReturn(Optional.of(room));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> movieService.scheduleMovie(movieScheduleDto));
        assertEquals("The room is not available at this hour!", badRequestException.getMessage());
    }
}
