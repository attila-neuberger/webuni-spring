package hu.webuni.hr.comtur.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.comtur.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findBySalaryGreaterThan(int salary);
	
	List<Employee> findByPosition(String position);
	
	List<Employee> findByNameIgnoreCaseStartingWith(String name);
	
	List<Employee> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
}
