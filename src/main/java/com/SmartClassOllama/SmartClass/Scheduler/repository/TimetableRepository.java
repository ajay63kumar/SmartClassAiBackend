package com.SmartClassOllama.SmartClass.Scheduler.repository;

import com.SmartClassOllama.SmartClass.Scheduler.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
    List<Timetable> findByClassName(String className);
}
