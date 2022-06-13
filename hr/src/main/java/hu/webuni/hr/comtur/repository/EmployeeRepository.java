package hu.webuni.hr.comtur.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import hu.webuni.hr.comtur.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

	List<Employee> findBySalaryGreaterThan(int salary);
	
	/**
	 * Finds pageable employees by position attribute's name attribute.
	 * @param position Position name.
	 * @param pageable {@link Pageable} object.
	 * @return List of employees with parametric position name.
	 */
	Page<Employee> findByPositionName(String positionName, Pageable pageable);
	
	List<Employee> findByNameIgnoreCaseStartingWith(String name);
	
	List<Employee> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
}
