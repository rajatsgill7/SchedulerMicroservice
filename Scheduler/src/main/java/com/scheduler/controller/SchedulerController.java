package com.scheduler.controller;

import com.scheduler.model.Appointment;
import com.scheduler.model.Operator;
import com.scheduler.model.Schedule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.scheduler.service.SchedulerService;

import java.util.List;

@RestController
@RequestMapping("/scheduler")
public class SchedulerController {
    @Autowired
    private SchedulerService schedulerService;

    @GetMapping("/getAllSchedules")
    public ResponseEntity<List<Schedule>> getAllSchedules(){
        return new ResponseEntity<>(schedulerService.getAllSchedules(),HttpStatus.OK);
    }
    @GetMapping("/getAllOperators")
    public ResponseEntity<List<Operator>> getAllOperators(){
        return new ResponseEntity<>(schedulerService.getAllOperators(),HttpStatus.OK);
    }
    @GetMapping("/getOperator/{id}")
    public ResponseEntity<Operator> getOperator(@PathVariable("id") Integer id){
        return new ResponseEntity<>(schedulerService.getOperator(id),HttpStatus.OK);
    }
    @PostMapping("/bookAppointment")
    public ResponseEntity<Object> bookAppointment(@Validated @RequestBody Schedule booking) {
        Schedule scheduled = schedulerService.bookAppointment(booking);
        if(scheduled == null){
            return new ResponseEntity<>("No Operator available",HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(scheduled, HttpStatus.CREATED);
    }
    @PatchMapping("/rescheduleAppointment/{id}")
    public ResponseEntity<Schedule> rescheduleAppointment(@Validated @RequestBody Schedule booking, @PathVariable("id") int id){
        Schedule rescheduled = schedulerService.rescheduleAppointment(booking,id);
        return new ResponseEntity<>(rescheduled, HttpStatus.ACCEPTED);
    }
    @DeleteMapping("/cancelAppointment")
    public ResponseEntity<String> cancelAppointment(@RequestBody Appointment appointment){
        return new ResponseEntity<>(schedulerService.cancelAppointment(appointment),HttpStatus.ACCEPTED);
    }
}
