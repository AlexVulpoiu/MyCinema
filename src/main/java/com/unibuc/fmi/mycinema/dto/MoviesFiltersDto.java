package com.unibuc.fmi.mycinema.dto;

import com.unibuc.fmi.mycinema.enums.EGenre;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MoviesFiltersDto {

    private String name;

    private LocalDate date;

    private EGenre genre;
}
