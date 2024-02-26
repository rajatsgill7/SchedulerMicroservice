package com.scheduler.service;

import com.scheduler.model.Appointment;
import com.scheduler.model.Operator;
import com.scheduler.model.Schedule;
import com.scheduler.repository.AppointmentRepo;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.scheduler.repository.OperatorRepo;
import com.scheduler.repository.ScheduleRepo;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class ScheduleServiceImpl implements SchedulerService{

    @Autowired
    OperatorRepo operatorRepo;
    @Autowired
    ScheduleRepo scheduleRepo;
    @Autowired
    AppointmentRepo appointmentRepo;
    @Override
    public List<Schedule> getAllSchedules() {
        return scheduleRepo.findAll();
    }

    @Override
    public List<Operator> getAllOperators() {
        return operatorRepo.findAll();
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "bookAppointmentCircuitBreaker", fallbackMethod = "fallbackMethod")
    public Schedule bookAppointment(Schedule schedule) {
        Operator assignedOperator = operatorRepo.getMostAvailableOperator().stream().findAny().orElseThrow();
        if(assignedOperator.getAvailability() < 0){
            return null;
        }
        assignedOperator.setAvailability(assignedOperator.getAvailability()-schedule.getNumberOfHours());
        operatorRepo.save(assignedOperator);

        String scheduleString = "0";
        if(assignedOperator.getScheduleString()!=null && !assignedOperator.getScheduleString().equals("")){
            String [] schedules = StringUtils.split(assignedOperator.getScheduleString(),",");

                scheduleString=assignedOperator.getScheduleString();
            String lastAppointmentEndTime = scheduleString.replaceAll(",$", "").substring(scheduleString.lastIndexOf('-') + 1);
                int newAppointmentEndTime = Integer.parseInt(lastAppointmentEndTime)+ schedule.getNumberOfHours();
                scheduleString+=lastAppointmentEndTime+"-"+newAppointmentEndTime+",";
                assignedOperator.setScheduleString(scheduleString);
                operatorRepo.save(assignedOperator);

        }else{
            scheduleString += "-" +schedule.getNumberOfHours()+",";
            assignedOperator.setScheduleString(scheduleString);
            operatorRepo.save(assignedOperator);
        }

        schedule = scheduleRepo.save(schedule);
        appointmentRepo.save(new Appointment(ThreadLocalRandom.current().nextInt(100000, 1000000),assignedOperator.getOperatorId(),schedule.getScheduleId(),scheduleString));

        return schedule;
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "rescheduleAppointmentCircuitBreaker", fallbackMethod = "fallbackMethod")
    public Schedule rescheduleAppointment(Schedule schedule, int id) {
        Appointment appointment = appointmentRepo.findById(id).orElseThrow();
        String oldScheduleString = appointment.getScheduleString();
        Operator assignedOperator = operatorRepo.findById(appointment.getOperatorId()).orElseThrow();
        int lastTime = Integer.parseInt(oldScheduleString.trim().replaceAll("[, ]+$", "").substring(oldScheduleString.lastIndexOf('-') + 1));
        int newTime = lastTime + schedule.getNumberOfHours();
        assignedOperator.setAvailability(assignedOperator.getAvailability()-lastTime+newTime);
        String newScheduleString = oldScheduleString.replaceAll("-\\d+,", "-" + newTime + ",");
        String newOperatorScheduleString = Arrays.stream(assignedOperator.getScheduleString().split(","))
                .map(segment -> oldScheduleString.equals(segment+",") ? newScheduleString : segment)
                .collect(Collectors.joining(","));
        assignedOperator.setScheduleString(newOperatorScheduleString+",");
        appointmentRepo.save(new Appointment(ThreadLocalRandom.current().nextInt(100000, 1000000),
                assignedOperator.getOperatorId(),schedule.getScheduleId(),newScheduleString));


        return scheduleRepo.save(schedule);
    }

    @Override
    @Transactional
    @CircuitBreaker(name = "cancelAppointmentCircuitBreaker", fallbackMethod = "fallbackMethod")
    public String cancelAppointment(Appointment appointment) {
        try {
            Schedule schedule1 = scheduleRepo.findById(appointment.getScheduleId()).orElseThrow();
            Operator operator = operatorRepo.findById(appointment.getOperatorId()).orElseThrow();
            Appointment appointment1 = appointmentRepo.findById(appointment.getAppointmentId()).orElseThrow();
            List<String> stringList = new ArrayList<>(Arrays.asList(operator.getScheduleString().split(",")));
            stringList.remove(appointment1.getScheduleString().replace(",",""));
            operator.setScheduleString(String.join(",",stringList));
            operatorRepo.save(operator);

            appointmentRepo.delete(appointment1);

            scheduleRepo.delete(schedule1);
        }catch (Exception e){
            e.printStackTrace();
            return "Error in deleting appointment";
        }
        return "Appointment deleted successfully";
    }

    @Override
    @CircuitBreaker(name = "getOperatorCircuitBreaker", fallbackMethod = "fallbackMethod")
    public Operator getOperator(Integer id) {
        return operatorRepo.findById(id).orElse(null);
    }

    public String fallbackMethod(Exception ex) {
        // logic to execute when the circuit is open
        return "Fallback response";
    }
}
