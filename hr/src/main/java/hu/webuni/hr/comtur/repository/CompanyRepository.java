package hu.webuni.hr.comtur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	@Query(
			"SELECT DISTINCT e.company " +
			"FROM Employee e " +
			"WHERE e.salary >= :salary")
	List<Company> getCompaniesWithHighSalaryEmployee(int salary);
	
	@Query(
			"SELECT c " +
			"FROM Employee e " +
				"JOIN e.company c " +
			"GROUP BY c " +
			"HAVING COUNT(e.id) > :count")
	List<Company> getCompaniesWithEmployeesMoreThan(long count);
	
	@Query(
			"SELECT NEW hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries(e.position.name, AVG(e.salary)) " +
			"FROM Employee e " +
				"JOIN e.company c " +
			"WHERE c.id = :id " +
			"GROUP BY e.position.name " +
			"ORDER BY 2 DESC")
	List<CompanysAverageSalaries> getCompanysAverageSalaries(long id);
	
	/*@Query(
			"SELECT DISTINCT c " + 
			"FROM Company c " + 
				"LEFT JOIN FETCH c.employees")*/ // Fetch join example.
	@Query("SELECT c FROM Company c") // Entity graph example.
	@EntityGraph(attributePaths = {"employees", "companyType", "employees.position"}) // Entity graph example for attributes.
	// @EntityGraph("Company.full") // Entity graph example for named graph, see Company class's annotation.
	List<Company> getAllWithEmployees();
}
