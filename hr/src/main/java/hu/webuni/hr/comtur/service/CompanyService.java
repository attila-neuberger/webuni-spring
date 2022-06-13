package hu.webuni.hr.comtur.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries;
import hu.webuni.hr.comtur.repository.CompanyRepository;
import hu.webuni.hr.comtur.repository.EmployeeRepository;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class CompanyService extends BaseService<Company> {

	/**
	 * Additional repository for employees.
	 */
	@Autowired
	private EmployeeRepository employeeRepository;
	
	public CompanyService(CompanyRepository companyRepository) {
		super();
		this.repository = companyRepository;
	}
	
	@Transactional
	public Company createEmployeeForCompany(long companyId, Employee employee) throws NoSuchElementException {
		Company company = repository.findById(companyId).get();
		company.addEmployee(employee);
		employeeRepository.save(employee); // Creating employee.
		// Modifying company is unnecessary because employee contains companyId in DB.
		return company;
	}
	
	@Transactional
	public Company deleteEmployeeFromCompany(long companyId, long employeeId)  throws NoSuchElementException {
		Company company = repository.findById(companyId).get();
		Employee employee = employeeRepository.findById(employeeId).get();
		if (employee.getCompany() == null || employee.getCompany().getId() != companyId) {
			throw new NoSuchElementException("The employee cannot be found in the company.");
		}
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeRepository.save(employee); // Updating employee.
		// Modifying company is unnecessary because employee contains companyId in DB.
		return company;
	}
	
	@Transactional
	public Company swapEmployeesOfCompany(List<Employee> employees, long companyId) {
		Company company = repository.findById(companyId).get();
		for (Employee employee : company.getEmployees()) {
			employee.setCompany(null);
		}
		company.getEmployees().clear();
		for (Employee employee : employees) {
			company.addEmployee(employee);
			employeeRepository.save(employee);
		}
		return company;
	}
	
	public List<Company> getCompaniesWithHighSalaryEmployee(int salary) {
		return ((CompanyRepository)repository).getCompaniesWithHighSalaryEmployee(salary);
	}
	
	public List<Company> getCompaniesWithEmployeesMoreThan(long count) {
		return ((CompanyRepository)repository).getCompaniesWithEmployeesMoreThan(count);
	}
	
	public List<CompanysAverageSalaries> getCompanysAverageSalaries(long companyRegistrationNumber) throws NoSuchElementException {
		Company company = ((CompanyRepository)repository).findByCompanyRegistrationNumber(companyRegistrationNumber);
		if (company == null) {
			throw new NoSuchElementException(String.format("Company with ID %d does not exist.", companyRegistrationNumber));
		}
		return ((CompanyRepository)repository).getCompanysAverageSalaries(companyRegistrationNumber);
	}
	
	@Transactional
	public Company changeSalaryForPositionOfCompany(String positionName, int minSalary, long companyRegistrationNumber) {
		Company company = ((CompanyRepository)repository).findByCompanyRegistrationNumber(companyRegistrationNumber);
		if (company == null) {
			throw new NoSuchElementException(String.format("Company with ID %d does not exist.", companyRegistrationNumber));
		}
		for (Employee employee : company.getEmployees()) {
			if (employee.getPosition() != null && positionName.equals(employee.getPosition().getName())) {
				employee.getPosition().setMinSalary(minSalary);
				if (employee.getSalary() < minSalary) {
					employee.setSalary(minSalary);
				}
			}
		}
		return company;
	}
}
