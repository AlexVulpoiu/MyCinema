package com.unibuc.fmi.mycinema.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unibuc.fmi.mycinema.dto.ActorDetailsDto;
import com.unibuc.fmi.mycinema.dto.ActorDto;
import com.unibuc.fmi.mycinema.exception.BadRequestException;
import com.unibuc.fmi.mycinema.exception.EntityNotFoundException;
import com.unibuc.fmi.mycinema.exception.UniqueConstraintException;
import com.unibuc.fmi.mycinema.service.impl.ActorServiceImpl;
import com.unibuc.fmi.mycinema.utils.ActorMocks;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static com.unibuc.fmi.mycinema.constants.Constants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ActorController.class)
public class ActorControllerTest {

    @MockBean
    private final ActorServiceImpl actorService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    @Autowired
    public ActorControllerTest(ActorServiceImpl actorService, MockMvc mockMvc, ObjectMapper objectMapper) {
        this.actorService = actorService;
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    @Test
    public void getActorsTest() throws Exception {
        List<ActorDetailsDto> actors = new ArrayList<>();
        for(long i = 1; i <= 5; i++) {
            actors.add(ActorMocks.mockActorDetailsDto(i));
        }
        when(actorService.getActors()).thenReturn(actors);

        String actorsBody = objectMapper.writeValueAsString(actors);
        MvcResult result = mockMvc.perform(get("/actors"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), actorsBody);
    }

    @Test
    public void addActorTest() throws Exception {
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorService.add(any())).thenReturn(actorDto);

        String actorDtoBody = objectMapper.writeValueAsString(actorDto);
        MvcResult result = mockMvc.perform(post("/actors")
                .content(actorDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(actorDto.getName()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), actorDtoBody);
    }

    @Test
    public void addActorThrowsConflictExceptionTest() throws Exception {
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorService.add(any())).thenThrow(new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "actor", "name")));

        String actorDtoBody = objectMapper.writeValueAsString(actorDto);
        mockMvc.perform(post("/actors")
                        .content(actorDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void editActorTest() throws Exception {
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorService.editActor(any(), any())).thenReturn(actorDto);

        String actorDtoBody = objectMapper.writeValueAsString(actorDto);
        MvcResult result = mockMvc.perform(put("/actors/1")
                        .content(actorDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(actorDto.getName()))
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), actorDtoBody);
    }

    @Test
    public void editActorThrowsNotFoundExceptionTest() throws Exception {
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorService.editActor(any(), any())).thenThrow(new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "actor", "id", 1)));

        String actorDtoBody = objectMapper.writeValueAsString(actorDto);
        mockMvc.perform(put("/actors/1")
                        .content(actorDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void editActorThrowsConflictExceptionTest() throws Exception {
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorService.editActor(any(), any())).thenThrow(new UniqueConstraintException(String.format(UNIQUE_CONSTRAINT, "actor", "name")));

        String actorDtoBody = objectMapper.writeValueAsString(actorDto);
        mockMvc.perform(put("/actors/1")
                        .content(actorDtoBody).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict())
                .andReturn();
    }

    @Test
    public void deleteActorTest() throws Exception {
        ActorDto actorDto = ActorMocks.mockActorDto();
        when(actorService.deleteActor(any())).thenReturn(actorDto);

        String actorDtoBody = objectMapper.writeValueAsString(actorDto);
        MvcResult result = mockMvc.perform(delete("/actors/1"))
                .andExpect(status().isOk())
                .andReturn();
        assertEquals(result.getResponse().getContentAsString(), actorDtoBody);
    }

    @Test
    public void deleteActorThrowsNotFoundExceptionTest() throws Exception {
        when(actorService.deleteActor(any())).thenThrow(new EntityNotFoundException(String.format(ENTITY_NOT_FOUND, "actor", "id", 1)));

        mockMvc.perform(delete("/actors/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void deleteActorThrowsBasRequestExceptionTest() throws Exception {
        when(actorService.deleteActor(any())).thenThrow(new BadRequestException(ACTOR_HAS_ROLES_IN_MOVIES));

        mockMvc.perform(delete("/actors/1"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
