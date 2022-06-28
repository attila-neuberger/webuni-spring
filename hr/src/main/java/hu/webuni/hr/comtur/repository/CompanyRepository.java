package hu.webuni.hr.comtur.repository;

import java.util.List;
import java.util.Optional;

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
	
	/**
	 * Method getCompaniesWithHighSalaryEmployee has lazy initialization problem.
	 * This method is an eager type of the original, with a different SQL query logic.
	 * @param salary Salary threshold value.
	 * @return List of companies having employees with salary at least the parametric.
	 */
	@Query(
			"SELECT DISTINCT c " +
			"FROM Company c " + 
				"JOIN c.employees es " + 
			"WHERE es.salary >= :salary")
	@EntityGraph("Company.full")
	List<Company> getCompaniesWithHighSalaryEmployeeEager(int salary);
	
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
				"LEFT JOIN FETCH c.employees")*/ // With Fetch join.
	@Query(
			"SELECT c " + 
			"FROM Company c")
	@EntityGraph(attributePaths = {"employees", "companyType", "employees.position"})
	List<Company> getCompaniesWithEmployees();
	
	@Query(
			"SELECT c " + 
			"FROM Company c " + 
			"WHERE c.id = :id")
	@EntityGraph(attributePaths = {"employees", "companyType", "employees.position"})
	Optional<Company> getCompanyWithEmployees(long id);
}
