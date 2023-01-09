package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.validator.OnlyLetters;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActorDetailsDto {

    @NotNull(message = "Id can't be null!")
    private Long id;

    @NotBlank(message = "Actor name must contain at least one letter!")
    @OnlyLetters
    private String name;
}
