package com.unibuc.fmi.mycinema.entity;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movies_schedule")
public class MovieSchedule {

    @EmbeddedId
    private MovieScheduleId id;

    private Integer price;

    @MapsId("movieId")
    @ManyToOne
    private Movie movie;

    @MapsId("roomId")
    @ManyToOne
    private Room room;

    @OneToMany(mappedBy = "movieSchedule", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<Ticket> tickets = new ArrayList<>();
}
