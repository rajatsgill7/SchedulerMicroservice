package com.scheduler.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Operator")
public class Operator {
    @Id
    @GeneratedValue
    Integer operatorId;
    Integer availability ;
    String scheduleString;

}
