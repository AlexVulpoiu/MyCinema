package com.unibuc.fmi.mycinema.composed_id;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class MovieScheduleId implements Serializable {

    private Long movieId;

    private Long roomId;

    private LocalDate date;

    private LocalTime hour;
}
