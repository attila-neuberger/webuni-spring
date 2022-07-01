package hu.webuni.hr.comtur.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import hu.webuni.hr.comtur.model.VacationRequest;

public interface VacationRequestRepository extends JpaRepository<VacationRequest, Long>, JpaSpecificationExecutor<VacationRequest> {
	
	@Override
	Page<VacationRequest> findAll(Pageable pageable);

	@Override
	List<VacationRequest> findAll(Sort sort);
	
	Page<VacationRequest> findAllByOrderByVacationStart(Pageable pageable);
	
	/*List<Employee> findBySalaryGreaterThan(int salary);
	
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
	
	@Modifying
	@Transactional
	@Query(
			"UPDATE Employee e " +
			"SET e.company = NULL " + 
			"WHERE e.id IN (" + 
				"SELECT e2.id " +
				"FROM Employee e2 " +
				"WHERE e2.company.id = :companyId " + 
			")")
	int removeCompanyWithId(long companyId);
	
	@Query(
			"SELECT e " + 
			"FROM Employee e")
	@EntityGraph(attributePaths = {"position", "company", "company.companyType"})
	List<Employee> getEmployeesWithCompany();
	
	@Query(
			"SELECT e " + 
			"FROM Employee e " + 
			"WHERE e.id = :id")
	@EntityGraph(attributePaths = {"position", "company", "company.companyType"})
	Optional<Employee> getEmployeeWithCompany(long id);*/
}
