package hu.webuni.hr.comtur.service;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.CompanyType;
import hu.webuni.hr.comtur.model.Education;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.repository.CompanyRepository;

@SpringBootTest
@AutoConfigureTestDatabase
public class CompanyServiceIt {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private EmployeeService employeeService;
	
	@BeforeEach
	public void init() {
		// No cleanup needed.
	}
	
	@Test
	void testCreateEmployeeForCompany() throws Exception {
		long companyRegistrationNumber = 1L;
		String companyName = "Company 1";
		String companyAddress = "Company 1 address";
		String companyTypeName = "ZRT";
		long companyId = saveCompany(companyRegistrationNumber, companyName, companyAddress, companyTypeName);
		
		String employeeName = "Employee 1";
		int employeeSalary = 2000;
		LocalDateTime employeeStartDate = LocalDateTime.of(2022, 06, 20, 8, 0, 0);
		String employeePositionName = "Employee 1 position";
		Education employeeMinEducation = Education.UNIVERSITY;
		Employee employee = createEmployee(employeeName, employeeSalary, employeeStartDate, employeePositionName, employeeMinEducation);
		
		// Creating first employee for the company:
		Company companyWithCreatedEmployee = companyService.createEmployeeForCompany(companyId, employee);
		Optional<Company> companyInDb = companyService.getCompanyWithEmployees(companyWithCreatedEmployee.getId());
		
		assertThat(companyInDb).isNotNull();
		List<Employee> employees = companyInDb.get().getEmployees();
		assertThat(employees).isNotEmpty();
		assertThat(employees.size()).isEqualTo(1);
		assertThat(employees.get(0).getName()).isEqualTo(employeeName);
		assertThat(employees.get(0).getSalary()).isEqualTo(employeeSalary);
		assertThat(employees.get(0).getStartDate()).isCloseTo(employeeStartDate, within(1, ChronoUnit.MICROS));
		assertThat(employees.get(0).getPosition().getName()).isEqualTo(employeePositionName);
		assertThat(employees.get(0).getPosition().getMinEducation()).isEqualTo(employeeMinEducation);
		assertThat(employees.get(0).getCompany().getId()).isEqualTo(companyId);
		assertThat(employees.get(0).getCompany().getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
		assertThat(employees.get(0).getCompany().getName()).isEqualTo(companyName);
		assertThat(employees.get(0).getCompany().getAddress()).isEqualTo(companyAddress);
		assertThat(employees.get(0).getCompany().getCompanyType().getName()).isEqualTo(companyTypeName);
		
		String employeeName2 = "Employee 2";
		int employeeSalary2 = 2500;
		LocalDateTime employeeStartDate2 = LocalDateTime.of(2022, 06, 20, 9, 0, 0);
		String employeePositionName2 = "Employee 2 position";
		Education employeeMinEducation2 = Education.COLLEGE;
		Employee employee2 = createEmployee(employeeName2, employeeSalary2, employeeStartDate2, employeePositionName2, employeeMinEducation2);
		
		// Creating second employee for the company:
		Company companyWithCreatedEmployee2 = companyService.createEmployeeForCompany(companyId, employee2);
		Optional<Company> companyInDb2 = companyService.getCompanyWithEmployees(companyWithCreatedEmployee2.getId());
		
		assertThat(companyInDb2).isNotNull();
		List<Employee> employees2 = companyInDb2.get().getEmployees();
		assertThat(employees2).isNotEmpty();
		assertThat(employees2.size()).isEqualTo(2);
		Collections.sort(employees2);
		Employee employee2InList = employees2.get(employees2.size() - 1);
		assertThat(employee2InList.getName()).isEqualTo(employeeName2);
		assertThat(employee2InList.getSalary()).isEqualTo(employeeSalary2);
		assertThat(employee2InList.getStartDate()).isCloseTo(employeeStartDate2, within(1, ChronoUnit.MICROS));
		assertThat(employee2InList.getPosition().getName()).isEqualTo(employeePositionName2);
		assertThat(employee2InList.getPosition().getMinEducation()).isEqualTo(employeeMinEducation2);
		assertThat(employee2InList.getCompany().getId()).isEqualTo(companyId);
		assertThat(employee2InList.getCompany().getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
		assertThat(employee2InList.getCompany().getName()).isEqualTo(companyName);
		assertThat(employee2InList.getCompany().getAddress()).isEqualTo(companyAddress);
		assertThat(employee2InList.getCompany().getCompanyType().getName()).isEqualTo(companyTypeName);
	}
	
	@Test
	void testDeleteEmployeeFromCompany() throws Exception {
		long companyRegistrationNumber = 1L;
		String companyName = "Company 1";
		String companyAddress = "Company 1 address";
		String companyTypeName = "ZRT";
		long companyId = saveCompany(companyRegistrationNumber, companyName, companyAddress, companyTypeName);
		
		String employeeName = "Employee 1";
		int employeeSalary = 2000;
		LocalDateTime employeeStartDate = LocalDateTime.of(2022, 06, 20, 8, 0, 0);
		String employeePositionName = "Employee 1 position";
		Education employeeMinEducation = Education.UNIVERSITY;
		Employee employee = createEmployee(employeeName, employeeSalary, employeeStartDate, employeePositionName, employeeMinEducation);
		
		// Creating first employee for the company:
		companyService.createEmployeeForCompany(companyId, employee);
		
		String employeeName2 = "Employee 2";
		int employeeSalary2 = 2500;
		LocalDateTime employeeStartDate2 = LocalDateTime.of(2022, 06, 20, 9, 0, 0);
		String employeePositionName2 = "Employee 2 position";
		Education employeeMinEducation2 = Education.COLLEGE;
		Employee employee2 = createEmployee(employeeName2, employeeSalary2, employeeStartDate2, employeePositionName2, employeeMinEducation2);
		
		// Creating second employee for the company:
		companyService.createEmployeeForCompany(companyId, employee2);
		Optional<Company> companyInDb = companyService.getCompanyWithEmployees(companyId);
		
		assertThat(companyInDb).isNotNull();
		List<Employee> employees = companyInDb.get().getEmployees();
		assertThat(employees).isNotEmpty();
		assertThat(employees.size()).isEqualTo(2);
		
		// Deleting first employee from the company:
		companyService.deleteEmployeeFromCompany(companyId, employee.getId());
		Optional<Company> companyInDb2 = companyService.getCompanyWithEmployees(companyId);
		
		assertThat(companyInDb2).isNotNull();
		List<Employee> employees2 = companyInDb2.get().getEmployees();
		assertThat(employees2).isNotEmpty();
		assertThat(employees2.size()).isEqualTo(1);
		assertThat(employees2.get(0).getName()).isEqualTo(employeeName2);
		assertThat(employees2.get(0).getSalary()).isEqualTo(employeeSalary2);
		assertThat(employees2.get(0).getStartDate()).isCloseTo(employeeStartDate2, within(1, ChronoUnit.MICROS));
		assertThat(employees2.get(0).getPosition().getName()).isEqualTo(employeePositionName2);
		assertThat(employees2.get(0).getPosition().getMinEducation()).isEqualTo(employeeMinEducation2);
		assertThat(employees2.get(0).getCompany().getId()).isEqualTo(companyId);
		assertThat(employees2.get(0).getCompany().getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
		assertThat(employees2.get(0).getCompany().getName()).isEqualTo(companyName);
		assertThat(employees2.get(0).getCompany().getAddress()).isEqualTo(companyAddress);
		assertThat(employees2.get(0).getCompany().getCompanyType().getName()).isEqualTo(companyTypeName);
		
		Optional<Employee> employeeRemovedFromCompany = employeeService.findById(employee.getId());
		assertThat(employeeRemovedFromCompany).isNotEmpty();
		assertThat(employeeRemovedFromCompany.get().getCompany()).isNull();
	}
	
	@Test
	void testSwapEmployeesOfCompany() throws Exception {
		long companyRegistrationNumber = 1L;
		String companyName = "Company 1";
		String companyAddress = "Company 1 address";
		String companyTypeName = "ZRT";
		long companyId = saveCompany(companyRegistrationNumber, companyName, companyAddress, companyTypeName);
		
		String employeeName = "Employee 1";
		int employeeSalary = 2000;
		LocalDateTime employeeStartDate = LocalDateTime.of(2022, 06, 20, 8, 0, 0);
		String employeePositionName = "Employee 1 position";
		Education employeeMinEducation = Education.UNIVERSITY;
		Employee employee = createEmployee(employeeName, employeeSalary, employeeStartDate, employeePositionName, employeeMinEducation);
		
		// Creating employee for the company:
		Company companyWithCreatedEmployee = companyService.createEmployeeForCompany(companyId, employee);
		Optional<Company> companyInDb = companyService.getCompanyWithEmployees(companyWithCreatedEmployee.getId());
		
		assertThat(companyInDb).isNotNull();
		List<Employee> employees = companyInDb.get().getEmployees();
		assertThat(employees).isNotEmpty();
		assertThat(employees.size()).isEqualTo(1);
		assertThat(employees.get(0).getName()).isEqualTo(employeeName);
		assertThat(employees.get(0).getSalary()).isEqualTo(employeeSalary);
		assertThat(employees.get(0).getStartDate()).isCloseTo(employeeStartDate, within(1, ChronoUnit.MICROS));
		assertThat(employees.get(0).getPosition().getName()).isEqualTo(employeePositionName);
		assertThat(employees.get(0).getPosition().getMinEducation()).isEqualTo(employeeMinEducation);
		assertThat(employees.get(0).getCompany().getId()).isEqualTo(companyId);
		assertThat(employees.get(0).getCompany().getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
		assertThat(employees.get(0).getCompany().getName()).isEqualTo(companyName);
		assertThat(employees.get(0).getCompany().getAddress()).isEqualTo(companyAddress);
		assertThat(employees.get(0).getCompany().getCompanyType().getName()).isEqualTo(companyTypeName);
		
		List<Employee> newEmployees = new ArrayList<>(2);
		
		String employeeName2 = "Employee 2";
		int employeeSalary2 = 2500;
		LocalDateTime employeeStartDate2 = LocalDateTime.of(2022, 06, 20, 9, 0, 0);
		String employeePositionName2 = "Employee 2 position";
		Education employeeMinEducation2 = Education.COLLEGE;
		Employee employee2 = createEmployee(employeeName2, employeeSalary2, employeeStartDate2, employeePositionName2, employeeMinEducation2);
		newEmployees.add(employee2);
		
		String employeeName3 = "Employee 3";
		int employeeSalary3 = 3000;
		LocalDateTime employeeStartDate3 = LocalDateTime.of(2022, 06, 20, 10, 0, 0);
		String employeePositionName3 = "Employee 3 position";
		Education employeeMinEducation3 = Education.GRADUATION;
		Employee employee3 = createEmployee(employeeName3, employeeSalary3, employeeStartDate3, employeePositionName3, employeeMinEducation3);
		newEmployees.add(employee3);
		
		// Swapping list of employees in the company:
		Company companyWithCreatedEmployee2 = companyService.swapEmployeesOfCompany(newEmployees, companyId);
		Optional<Company> companyInDb2 = companyService.getCompanyWithEmployees(companyWithCreatedEmployee2.getId());
		
		assertThat(companyInDb2).isNotNull();
		List<Employee> employees2 = companyInDb2.get().getEmployees();
		assertThat(employees2).isNotEmpty();
		assertThat(employees2.size()).isEqualTo(2);
		Collections.sort(employees2);
		assertThat(employees2.get(0).getName()).isEqualTo(employeeName2);
		assertThat(employees2.get(0).getSalary()).isEqualTo(employeeSalary2);
		assertThat(employees2.get(0).getStartDate()).isCloseTo(employeeStartDate2, within(1, ChronoUnit.MICROS));
		assertThat(employees2.get(0).getPosition().getName()).isEqualTo(employeePositionName2);
		assertThat(employees2.get(0).getPosition().getMinEducation()).isEqualTo(employeeMinEducation2);
		assertThat(employees2.get(0).getCompany().getId()).isEqualTo(companyId);
		assertThat(employees2.get(0).getCompany().getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
		assertThat(employees2.get(0).getCompany().getName()).isEqualTo(companyName);
		assertThat(employees2.get(0).getCompany().getAddress()).isEqualTo(companyAddress);
		assertThat(employees2.get(0).getCompany().getCompanyType().getName()).isEqualTo(companyTypeName);
		
		assertThat(employees2.get(1).getName()).isEqualTo(employeeName3);
		assertThat(employees2.get(1).getSalary()).isEqualTo(employeeSalary3);
		assertThat(employees2.get(1).getStartDate()).isCloseTo(employeeStartDate3, within(1, ChronoUnit.MICROS));
		assertThat(employees2.get(1).getPosition().getName()).isEqualTo(employeePositionName3);
		assertThat(employees2.get(1).getPosition().getMinEducation()).isEqualTo(employeeMinEducation3);
		assertThat(employees2.get(1).getCompany().getId()).isEqualTo(companyId);
		assertThat(employees2.get(1).getCompany().getCompanyRegistrationNumber()).isEqualTo(companyRegistrationNumber);
		assertThat(employees2.get(1).getCompany().getName()).isEqualTo(companyName);
		assertThat(employees2.get(1).getCompany().getAddress()).isEqualTo(companyAddress);
		assertThat(employees2.get(1).getCompany().getCompanyType().getName()).isEqualTo(companyTypeName);
		
		Optional<Employee> employeeRemovedFromCompany = employeeService.findById(employee.getId());
		assertThat(employeeRemovedFromCompany).isNotEmpty();
		assertThat(employeeRemovedFromCompany.get().getCompany()).isNull();
	}
	
	private long saveCompany(long companyRegistrationNumber, String name, String address, String companyTypeName) {
		CompanyType companyType = createCompanyType(companyTypeName);
		return companyRepository.save(new Company(companyRegistrationNumber, name, companyType, address)).getId();
	}
	
	private CompanyType createCompanyType(String name) {
		return new CompanyType(name);
	}
	
	private Employee createEmployee(String name, int salary, LocalDateTime startDate, String positionName, 
			Education positionMinEducation) {
		Position position = createPosition(positionName, positionMinEducation);
		return new Employee(name, position, salary, startDate);
	}
	
	private Position createPosition(String name, Education minEducation) {
		return new Position(name, minEducation);
	}
}
