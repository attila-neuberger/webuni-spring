package hu.webuni.hr.comtur.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.PositionXCompany;
import hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries;
import hu.webuni.hr.comtur.repository.CompanyRepository;
import hu.webuni.hr.comtur.repository.PositionXCompanyRepository;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public class CompanyService extends BaseService<Company> {

	/**
	 * Service for employees.
	 */
	@Autowired
	private EmployeeService employeeService;
	
	/**
	 * Additional repository for position_x_companies.
	 */
	@Autowired
	private PositionXCompanyRepository positionXCompanyRepository;
	
	public CompanyService(CompanyRepository companyRepository) {
		super();
		this.repository = companyRepository;
	}
	
	@Transactional
	public Company createEmployeeForCompany(long companyId, Employee employee) throws NoSuchElementException {
		Company company = repository.findById(companyId).get();
		employeeService.save(employee); // Creating employee.
		company.addEmployee(employee);
		// Modifying company is unnecessary because employee contains companyId in DB.
		return company;
	}
	
	@Transactional
	public Company deleteEmployeeFromCompany(long companyId, long employeeId)  throws NoSuchElementException {
		Company company = repository.findById(companyId).get();
		Employee employee = employeeService.findById(employeeId).get();
		if (employee.getCompany() == null || employee.getCompany().getId() != companyId) {
			throw new NoSuchElementException("The employee cannot be found in the company.");
		}
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeService.save(employee); // Updating employee.
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
			employeeService.save(employee);
		}
		return company;
	}
	
	public List<Company> getCompaniesWithHighSalaryEmployee(int salary) {
		return ((CompanyRepository)repository).getCompaniesWithHighSalaryEmployee(salary);
	}
	
	public List<Company> getCompaniesWithEmployeesMoreThan(long count) {
		return ((CompanyRepository)repository).getCompaniesWithEmployeesMoreThan(count);
	}
	
	public List<CompanysAverageSalaries> getCompanysAverageSalaries(long id) throws NoSuchElementException {
		Optional<Company> company = ((CompanyRepository)repository).findById(id);
		if (!company.isPresent()) {
			throw new NoSuchElementException(String.format("Company with ID %d does not exist.", id));
		}
		return ((CompanyRepository)repository).getCompanysAverageSalaries(id);
	}
	
	@Transactional
	public void changeSalaryForPosition(String positionName, int minSalary) {
		List<PositionXCompany> positionXCompanies = positionXCompanyRepository.findByPositionName(positionName);
		for (PositionXCompany positionXCompany : positionXCompanies) {
			positionXCompany.setMinSalary(minSalary);
		}
		/*List<Employee> employees = employeeRepository.findByPositionName(positionName);
		for (Employee employee : employees) {
			if (employee.getSalary() < minSalary) {
				employee.setSalary(minSalary);
			}
		}*/
		
		// 1 update instead of employees.size() update.
		employeeService.getRepository().changeSalaries(positionName, minSalary);
	}
	
	@Transactional
	public Company changeSalaryForPositionOfCompany(String positionName, int minSalary, long companyId) {
		Optional<PositionXCompany> positionXCompany = positionXCompanyRepository.findByPositionNameAndCompanyId(positionName, companyId);
		if (!positionXCompany.isPresent()) {
			throw new NoSuchElementException(String.format("Position with name '%s' in company with ID %d does not exist.", 
					positionName, companyId));
		}
		positionXCompany.get().setMinSalary(minSalary);
		/*for (Employee employee : positionXCompany.get().getCompany().getEmployees()) {
			if (positionName.equals(employee.getPosition().getName()) && employee.getSalary() < minSalary) {
				employee.setSalary(minSalary);
			}
		}*/
		
		// 1 update instead of positionXCompany.get().getCompany().getEmployees().size() update.
		employeeService.getRepository().changeSalaries(positionName, minSalary, companyId);
		return positionXCompany.get().getCompany();
	}

	@Override
	public Company save(Company company) {
		for (Employee employee : company.getEmployees()) {
			employee.setCompany(company);
		}
		employeeService.saveAll(company.getEmployees());
		return super.save(company);
	}
}
