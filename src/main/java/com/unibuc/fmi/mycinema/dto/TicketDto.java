package com.unibuc.fmi.mycinema.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    @NotNull(message = "The row must be provided!")
    @Min(value = 1, message = "Row number must be positive!")
    private Integer row;

    @NotNull(message = "The seat must be provided!")
    @Min(value = 1, message = "Seat number must be positive!")
    private Integer seat;
}
