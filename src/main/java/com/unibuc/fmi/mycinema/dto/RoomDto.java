package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.validator.OnlyLettersAndDigits;
import jakarta.validation.constraints.Min;
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
public class RoomDto {

    @NotBlank(message = "Room name must contain at least one letter or digit!")
    @OnlyLettersAndDigits
    private String name;

    @NotNull
    @Min(value = 1, message = "The cinema room must have minimum 1 row!")
    private Integer numberOfRows;

    @NotNull
    @Min(value = 1, message = "Each row must have minimum 1 seat!")
    private Integer seatsPerRow;
}
