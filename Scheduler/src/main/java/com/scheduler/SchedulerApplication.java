package com.scheduler;

import com.scheduler.model.Operator;
import com.scheduler.repository.OperatorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class SchedulerApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SchedulerApplication.class, args);
	}

	@Autowired
	OperatorRepo operatorRepo;
	@Override
	public void run(String... args) throws Exception {
		List<Operator> op = new ArrayList<>();
		for(int i= 1 ; i <= 7; i++){
			op.add(new Operator(i,24,null));
		}
		operatorRepo.saveAll(op);
	}
}