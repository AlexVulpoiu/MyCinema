package com.unibuc.fmi.mycinema.repository;

import com.unibuc.fmi.mycinema.composed_id.MovieScheduleId;
import com.unibuc.fmi.mycinema.entity.MovieSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MovieScheduleRepository extends JpaRepository<MovieSchedule, MovieScheduleId> {
}
