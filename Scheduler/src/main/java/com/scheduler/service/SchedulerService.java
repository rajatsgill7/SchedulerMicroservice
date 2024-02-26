package com.scheduler.service;

import com.scheduler.model.Appointment;
import com.scheduler.model.Operator;
import com.scheduler.model.Schedule;

import java.util.List;

public interface SchedulerService {


    List<Schedule> getAllSchedules();
    List<Operator> getAllOperators();

    Schedule bookAppointment(Schedule schedule);

    Schedule rescheduleAppointment(Schedule schedule, int id);

    String cancelAppointment (Appointment schedule);

    Operator getOperator(Integer id);
}
