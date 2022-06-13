package hu.webuni.hr.comtur.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries;

public interface CompanyRepository extends JpaRepository<Company, Long> {

	Company findByCompanyRegistrationNumber(long companyRegistrationNumber);
	
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
	
	/**
	 * Average salary of company based on COMPANY REGISTRATION NUMBER, group by positions.
	 * @param companyRegistrationNumber Value of company registration number.
	 * @return Result of the query.
	 */
	@Query(
			"SELECT NEW hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries(e.position.name, AVG(e.salary)) " +
			"FROM Employee e " +
				"JOIN e.company c " +
			"WHERE c.companyRegistrationNumber = :companyRegistrationNumber " +
			"GROUP BY e.position.name " +
			"ORDER BY 2 DESC")
	List<CompanysAverageSalaries> getCompanysAverageSalaries(long companyRegistrationNumber);
}
