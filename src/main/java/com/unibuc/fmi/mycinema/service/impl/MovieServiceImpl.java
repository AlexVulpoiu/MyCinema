package com.unibuc.fmi.mycinema.service.impl;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinema.dto.MovieDetailsDto;
import com.unibuc.fmi.mycinema.dto.MovieDto;
import com.unibuc.fmi.mycinema.dto.MovieScheduleDto;
import com.unibuc.fmi.mycinema.dto.NewMovieDto;
import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.entity.Movie;
import com.unibuc.fmi.mycinema.entity.MovieSchedule;
import com.unibuc.fmi.mycinema.entity.Room;
import com.unibuc.fmi.mycinema.enums.EGenre;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.MovieMapper;
import com.unibuc.fmi.mycinema.mapper.MovieScheduleMapper;
import com.unibuc.fmi.mycinema.repository.ActorRepository;
import com.unibuc.fmi.mycinema.repository.MovieRepository;
import com.unibuc.fmi.mycinema.repository.MovieScheduleRepository;
import com.unibuc.fmi.mycinema.repository.RoomRepository;
import com.unibuc.fmi.mycinema.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;

    private final ActorRepository actorRepository;

    private final RoomRepository roomRepository;

    private final MovieScheduleRepository movieScheduleRepository;

    private final MovieMapper movieMapper;

    private final MovieScheduleMapper movieScheduleMapper;

    @Autowired
    public MovieServiceImpl(MovieRepository movieRepository, ActorRepository actorRepository, RoomRepository roomRepository, MovieScheduleRepository movieScheduleRepository, MovieMapper movieMapper, MovieScheduleMapper movieScheduleMapper) {
        this.movieRepository = movieRepository;
        this.actorRepository = actorRepository;
        this.roomRepository = roomRepository;
        this.movieScheduleRepository = movieScheduleRepository;
        this.movieMapper = movieMapper;
        this.movieScheduleMapper = movieScheduleMapper;
    }

    @Override
    public MovieDetailsDto addMovie(NewMovieDto newMovieDto) {
        Optional<Movie> optionalMovie = movieRepository.findByName(newMovieDto.getName());
        if(optionalMovie.isPresent()) {
            throw new UniqueConstraintException("There is already a movie with the same name!");
        }

        List<Actor> actors = new ArrayList<>();
        for(Long actorId : newMovieDto.getActorIds()) {
            Optional<Actor> optionalActor = actorRepository.findById(actorId);
            if(optionalActor.isEmpty()) {
                throw new EntityNotFoundException("There is no actor with id: " + actorId + "!");
            }
            actors.add(optionalActor.get());
        }
        MovieDto movieDto = movieMapper.mapToMovieDto(newMovieDto, actors);
        Movie movie = movieRepository.save(movieMapper.mapToMovie(movieDto));
        movie.setSchedules(new ArrayList<>());

        return movieMapper.mapToMovieDetailsDto(movie);
    }

    @Override
    public List<MovieDetailsDto> searchMovies(String name, LocalDate date, EGenre genre) {
        List<MovieDetailsDto> movies = movieRepository.findAll().stream().map(movieMapper::mapToMovieDetailsDto).toList();

        if(name != null) {
            movies = movies.stream().filter(movie -> name.equals(movie.getName())).toList();
        }
        if(date != null) {
            for(MovieDetailsDto movie : movies) {
                List<MovieDetailsDto.ScheduleDetails> scheduleDetails =
                        new ArrayList<>(movie.getScheduleDetails().stream().filter(schedule -> date.equals(schedule.getDate())).toList());
                if(!scheduleDetails.isEmpty()) {
                    movie.setScheduleDetails(scheduleDetails);
                }
            }
        }
        if(genre != null) {
            movies = movies.stream().filter(movie -> genre.equals(movie.getGenre())).toList();
        }

        return movies;
    }

    @Override
    public MovieScheduleDto scheduleMovie(MovieScheduleDto movieScheduleDto) {
        String movieName = movieScheduleDto.getMovieName();
        Optional<Movie> optionalMovie = movieRepository.findByName(movieName);
        if(optionalMovie.isEmpty()) {
            throw new EntityNotFoundException("There is no movie with name: " + movieName + "!");
        }

        String roomName = movieScheduleDto.getRoomName();
        Optional<Room> optionalRoom = roomRepository.findByName(roomName);
        if(optionalRoom.isEmpty()) {
            throw new EntityNotFoundException("There is no room with name: " + roomName + "!");
        }

        Movie movie = optionalMovie.get();
        LocalDate date = movieScheduleDto.getDate();
        LocalTime hour = movieScheduleDto.getHour();
        LocalDateTime scheduleStartTime = LocalDateTime.of(date, hour);
        LocalDateTime scheduleEndTime = scheduleStartTime.plusMinutes(movie.getDuration());

        if(scheduleStartTime.isBefore(LocalDateTime.now())) {
            throw new BadRequestException("The movie can't be scheduled in a past date!");
        }

        Room room = optionalRoom.get();
        for(MovieSchedule movieSchedule : room.getSchedules()) {
            LocalDateTime startTime = LocalDateTime.of(movieSchedule.getId().getDate(), movieSchedule.getId().getHour());
            LocalDateTime endTime = startTime.plusMinutes(movieSchedule.getMovie().getDuration());
            if(scheduleStartTime.isBefore(endTime) && startTime.isBefore(scheduleEndTime)) {
                throw new BadRequestException("The room is not available at this hour!");
            }
        }

        MovieScheduleId movieScheduleId = MovieScheduleId.builder()
                .movieId(movie.getId())
                .roomId(room.getId())
                .date(date)
                .hour(hour)
                .build();
        MovieSchedule movieSchedule = MovieSchedule.builder()
                .id(movieScheduleId)
                .price(movieScheduleDto.getPrice())
                .movie(movie)
                .room(room)
                .build();
        return movieScheduleMapper.mapToMovieScheduleDto(movieScheduleRepository.save(movieSchedule));
    }
}
