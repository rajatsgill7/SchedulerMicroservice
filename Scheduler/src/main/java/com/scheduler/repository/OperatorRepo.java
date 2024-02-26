package com.scheduler.repository;

import com.scheduler.model.Operator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OperatorRepo extends JpaRepository<Operator,Integer> {

    @Query(value = "SELECT * FROM Operator WHERE availability = (SELECT MAX(availability) FROM Operator) ", nativeQuery = true)
    List<Operator> getMostAvailableOperator();
}
