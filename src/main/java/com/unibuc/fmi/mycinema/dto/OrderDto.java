package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.validator.OnlyLettersAndDigits;
import jakarta.validation.constraints.*;
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
public class OrderDto {

    @NotBlank(message = "The email address must be provided!")
    @Email(message = "The email should have an adequate format!")
    private String customerEmail;

    @NotNull(message = "The number of tickets must be provided!")
    @Min(value = 1, message = "The number of tickets must be at least 1!")
    private Integer numberOfTickets;

    @NotBlank(message = "Movie name can't be blank!")
    private String movieName;

    @NotBlank(message = "Room name must contain at least one letter or digit!")
    @OnlyLettersAndDigits
    private String roomName;

    @NotNull(message = "The date must be provided!")
    @FutureOrPresent(message = "You can't select a past date")
    private LocalDate movieDate;

    @NotNull(message = "The hour must be provided!")
    private LocalTime movieHour;
}
