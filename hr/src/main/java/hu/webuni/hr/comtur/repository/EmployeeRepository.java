package hu.webuni.hr.comtur.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

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
	
	List<Employee> findByPositionName(String positionName);
	
	List<Employee> findByNameIgnoreCaseStartingWith(String name);
	
	List<Employee> findByStartDateBetween(LocalDateTime from, LocalDateTime to);
	
	@Modifying
	@Transactional
	@Query(
			"UPDATE Employee e " +
			"SET e.salary = :minSalary " + 
			"WHERE e.id IN (" + 
				"SELECT e2.id " +
				"FROM Employee e2 " +
				"WHERE e2.position.name = :position " + 
					"AND e2.salary < :minSalary" + 
			")")
	int changeSalaries(String position, int minSalary);
	
	@Modifying
	@Transactional
	@Query(
			"UPDATE Employee e " +
			"SET e.salary = :minSalary " + 
			"WHERE e.id IN (" + 
				"SELECT e2.id " +
				"FROM Employee e2 " +
				"WHERE e2.position.name = :position " + 
					"AND e2.company.id = :companyId " + 
					"AND e2.salary < :minSalary" + 
			")")
	int changeSalaries(String position, int minSalary, long companyId);
}
