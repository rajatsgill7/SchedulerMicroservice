package com.scheduler.repository;

import com.scheduler.model.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepo extends JpaRepository<Appointment,Integer> {
}
