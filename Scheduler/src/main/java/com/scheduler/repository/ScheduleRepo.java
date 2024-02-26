package com.scheduler.repository;

import com.scheduler.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepo extends JpaRepository<Schedule,Integer> {
}
