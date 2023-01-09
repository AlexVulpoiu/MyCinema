package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.enums.EGenre;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MovieDetailsDto {

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
    @Size(min = 1, message = "Actors list can't be empty!")
    private List<ActorDto> actors;

    @NotNull(message = "Schedule details must be provided!")
    private List<ScheduleDetails> scheduleDetails;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScheduleDetails {
        private LocalDate date;
        private LocalTime hour;
        private Integer price;
        private String roomName;
    }
}
