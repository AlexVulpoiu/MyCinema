package com.unibuc.fmi.mycinema.service.impl;

import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.mapper.ActorMapper;
import com.unibuc.fmi.mycinema.repository.ActorRepository;
import com.unibuc.fmi.mycinema.service.ActorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ActorServiceImpl implements ActorService {

    private final ActorRepository actorRepository;

    private final ActorMapper actorMapper;

    @Autowired
    public ActorServiceImpl(ActorRepository actorRepository, ActorMapper actorMapper) {
        this.actorRepository = actorRepository;
        this.actorMapper = actorMapper;
    }

    @Override
    public List<ActorDetailsDto> getActors() {
        return actorRepository.findAll().stream().map(actorMapper::mapToActorDetailsDto).collect(Collectors.toList());
    }

    @Override
    public ActorDto addActor(ActorDto actorDto) {
        Optional<Actor> optionalActor = actorRepository.findByName(actorDto.getName());
        if(optionalActor.isPresent()) {
            throw new UniqueConstraintException("There is already an actor with the same name!");
        }
        return actorMapper.mapToActorDto(actorRepository.save(actorMapper.mapToActor(actorDto)));
    }

    @Override
    public ActorDto editActor(Long id, ActorDto actorDto) {
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if(optionalActor.isEmpty()) {
            throw new EntityNotFoundException("There is no actor with id: " + id + "!");
        }

        Optional<Actor> optionalActorByName = actorRepository.findByName(actorDto.getName());
        if(optionalActorByName.isPresent() && !Objects.equals(optionalActorByName.get().getId(), id)) {
            throw new UniqueConstraintException("There is already an actor with the same name!");
        }

        Actor actor = optionalActor.get();
        actor.setName(actorDto.getName());

        return actorMapper.mapToActorDto(actorRepository.save(actor));
    }

    @Override
    public ActorDto deleteActor(Long id) {
        Optional<Actor> optionalActor = actorRepository.findById(id);
        if(optionalActor.isEmpty()) {
            throw new EntityNotFoundException("There is no actor with id: " + id + "!");
        }

        Actor actor = optionalActor.get();

        if(!actor.getMovies().isEmpty()) {
            throw new BadRequestException("This actor can't be deleted because he has roles in some movies!");
        }

        ActorDto actorDto = actorMapper.mapToActorDto(actor);
        actorRepository.deleteById(id);

        return actorDto;
    }
}
