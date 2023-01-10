package com.unibuc.fmi.mycinema.service;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.entity.Movie;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.ActorMapper;
import com.unibuc.fmi.mycinema.repository.ActorRepository;
import com.unibuc.fmi.mycinema.service.impl.ActorServiceImpl;
import com.unibuc.fmi.mycinema.utils.ActorMocks;
import com.unibuc.fmi.mycinema.utils.MovieMocks;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.unibuc.fmi.mycinema.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ActorServiceImplTest {

    @InjectMocks
    private ActorServiceImpl actorService;

    @Mock
    private ActorRepository actorRepository;

    @Mock
    private ActorMapper actorMapper;

    @Test
    public void getActorsTest() {
        ActorDetailsDto actorDetailsDto = ActorMocks.mockActorDetailsDto();
        List<ActorDetailsDto> actorDetailsDtoList = List.of(actorDetailsDto);
        Actor actor = ActorMocks.mockActor();
        List<Actor> actors = List.of(actor);

        when(actorRepository.findAll()).thenReturn(actors);
        when(actorMapper.mapToActorDetailsDto(actor)).thenReturn(actorDetailsDto);

        List<ActorDetailsDto> result = actorService.getActors();
        assertEquals(result, actorDetailsDtoList);
    }

    @Test
    public void addActorTest() {
        Actor actor = ActorMocks.mockActor();
        ActorDto actorDto = ActorMocks.mockActorDto();

        when(actorRepository.save(actor)).thenReturn(actor);
        when(actorMapper.mapToActor(actorDto)).thenReturn(actor);
        when(actorMapper.mapToActorDto(actor)).thenReturn(actorDto);
        ActorDto result = actorService.add(actorDto);

        assertEquals(result.getName(), actorDto.getName());
    }

    @Test
    public void addActorThrowsUniqueConstraintExceptionTest() {
        Actor actor = ActorMocks.mockActor();
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorRepository.findByName("Test actor")).thenReturn(Optional.of(actor));

        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> actorService.add(actorDto));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "actor", "name"), uniqueConstraintException.getMessage());
    }

    @Test
    public void editActorTest() {
        Actor actor = ActorMocks.mockActor();
        ActorDto actorDto = ActorMocks.mockActorDto();
        actor.setName("Test actor edit");

        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        actorDto.setName("Test actor edit");
        when(actorMapper.mapToActorDto(actor)).thenReturn(actorDto);
        when(actorRepository.save(actor)).thenReturn(actor);
        ActorDto result = actorService.editActor(1L, actorDto);

        assertEquals(result.getName(), actorDto.getName());
        assertEquals(result.getName(), "Test actor edit");
    }

    @Test
    public void editActorThrowsEntityNotFoundExceptionTest() {
        Actor actor = ActorMocks.mockActor();
        ActorDto actorDto = ActorMocks.mockActorDto();
        actor.setName("Test actor edit");

        when(actorRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> actorService.editActor(1L, actorDto));
        assertEquals(String.format(ENTITY_NOT_FOUND, "actor", "id", 1), entityNotFoundException.getMessage());
    }

    @Test
    public void editActorThrowsUniqueConstraintExceptionTest() {
        Actor actor = ActorMocks.mockActor();
        ActorDto actorDto = ActorMocks.mockActorDto();
        actor.setName("Test actor edit");
        actorDto.setName("Test actor edit");
        Actor testActor = ActorMocks.mockActor();
        testActor.setId(2L);
        testActor.setName("Test actor edit");

        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        when(actorRepository.findByName("Test actor edit")).thenReturn(Optional.of(testActor));
        UniqueConstraintException uniqueConstraintException = assertThrows(UniqueConstraintException.class, () -> actorService.editActor(1L, actorDto));
        assertEquals(String.format(UNIQUE_CONSTRAINT, "actor", "name"), uniqueConstraintException.getMessage());
    }

    @Test
    public void deleteActorTest() {
        Actor actor = ActorMocks.mockActor();
        ActorDto actorDto = ActorMocks.mockActorDto();

        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        when(actorMapper.mapToActorDto(actor)).thenReturn(actorDto);
        ActorDto result = actorService.deleteActor(1L);

        assertEquals(result.getName(), actorDto.getName());
        assertEquals(result.getName(), "Test actor");
    }

    @Test
    public void deleteActorThrowsEntityNotFoundExceptionTest() {
        when(actorRepository.findById(1L)).thenReturn(Optional.empty());
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> actorService.deleteActor(1L));
        assertEquals(String.format(ENTITY_NOT_FOUND, "actor", "id", 1), entityNotFoundException.getMessage());
    }

    @Test
    public void deleteActorThrowsBadRequestExceptionTest() {
        Actor actor = ActorMocks.mockActor();
        Movie movie = MovieMocks.mockMovie();
        actor.getMovies().add(movie);

        when(actorRepository.findById(1L)).thenReturn(Optional.of(actor));
        BadRequestException badRequestException = assertThrows(BadRequestException.class, () -> actorService.deleteActor(1L));
        assertEquals(ACTOR_HAS_ROLES_IN_MOVIES, badRequestException.getMessage());
    }
}
