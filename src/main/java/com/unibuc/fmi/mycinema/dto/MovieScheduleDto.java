package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.validator.OnlyLettersAndDigits;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieScheduleDto {

    @NotBlank(message = "Movie name can't be null!")
    private String movieName;

    @NotBlank(message = "Room name can't be null!")
    @OnlyLettersAndDigits
    private String roomName;

    @NotNull(message = "Schedule date can't be null!")
    @FutureOrPresent(message = "Schedule date can't be a past date!")
    private LocalDate date;

    @NotNull(message = "Schedule hour can't be null!")
    private LocalTime hour;

    @NotNull(message = "Movie price must be provided!")
    @Min(value = 1, message = "Movie price must be a positive value!")
    private Integer price;
}
