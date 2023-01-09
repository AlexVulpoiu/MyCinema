package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.entity.Actor;
import com.unibuc.fmi.mycinema.enums.EGenre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDto {

    @NotBlank(message = "Movie name can't be blank!")
    private String name;

    @NotBlank(message = "Description can't be empty!")
    private String description;

    @NotNull(message = "Movie duration must be provided!")
    @Min(value = 1, message = "Movie duration must be a positive number!")
    private Integer duration;

    @NotNull(message = "Movie genre must be provided!")
    private EGenre genre;

    @NotNull(message = "Actors must be provided!")
    private List<Actor> actors;
}
