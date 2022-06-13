package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.CompanyType;
import hu.webuni.hr.comtur.model.Education;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries;
import hu.webuni.hr.comtur.repository.CompanyRepository;
import hu.webuni.hr.comtur.repository.EmployeeRepository;
import hu.webuni.hr.comtur.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private PositionRepository positionRepository;
	
	public void clearDatabase() {
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
		positionRepository.deleteAll();
	}
	
	public void insertTestData() {
		Company company1 = new Company(1L, "Attila programozócége", CompanyType.BT, "2800 Tatabánya, Fő u. 1.");
		Company company2 = new Company(42L, "Stopposok", CompanyType.ZRT, "Föld bolygó");
		Company company3 = new Company(3L, "Fox River", CompanyType.ZRT, "USA, Chicago, Illinois");
		Company company4 = new Company(4L, "Holt költők társasága", CompanyType.ZRT, "ismeretlen");
		Company company5 = new Company(5L, "Pici cég", CompanyType.KFT, "2112 Veresegyház, Fő u. 1.");
		
		companyRepository.save(company1);
		companyRepository.save(company2);
		companyRepository.save(company3);
		companyRepository.save(company4);
		companyRepository.save(company5);
		
		@SuppressWarnings("serial")
		List<Employee> employees = new ArrayList<>() {{
			
			// Company 1
			add(new Employee("Attila", new Position("Vezér", Education.UNIVERSITY, 2500), 4000, LocalDateTime.of(2010, 1, 1, 0, 0), company1));
			add(new Employee("Edit", new Position("Segítő", Education.GRADUATION, 1250), 2800, LocalDateTime.of(2018, 1, 1, 0, 0), company1));
			add(new Employee("Mici", new Position("Naplopó", Education.NONE, 100), 1000, LocalDateTime.of(2022, 1, 1, 0, 0), company1));
			
			// Company 2
			add(new Employee("Arthur Dent", new Position("Naplopó", Education.NONE, 100), 100, LocalDateTime.of(2020, 1, 1, 0, 0), company2));
			add(new Employee("Ford Prefect", new Position("Segítő", Education.GRADUATION, 1250), 10_000, LocalDateTime.of(2015, 1, 1, 0, 0), company2));
			add(new Employee("Trillian", new Position("Segítő", Education.GRADUATION, 1250), 11_000, LocalDateTime.of(2018, 1, 1, 0, 0), company2));
			add(new Employee("Zaphod Beeblebrox", new Position("Vezér", Education.COLLEGE, 10_000), 50_000_000, LocalDateTime.of(2005, 1, 1, 0, 0), company2));
			
			// Company 3
			add(new Employee("Michael Scofield", new Position("Segítő", null, 400), 500, LocalDateTime.of(2022, 1, 1, 0, 0), company3));
			add(new Employee("Lincoln Burrows", new Position("Naplopó", Education.NONE, 100), 400, LocalDateTime.of(2018, 1, 1, 0, 0), company3));
			add(new Employee("John Abruzzi", new Position("Vezér", Education.COLLEGE, 1000), 550, LocalDateTime.of(2014, 1, 1, 0, 0), company3));
			add(new Employee("Charles Westmoreland", new Position("Segítő", null, 400), 300, LocalDateTime.of(2005, 1, 1, 0, 0), company3));
			add(new Employee("Theodore Bagwell", new Position("Segítő", null, 400), 400, LocalDateTime.of(2016, 1, 1, 0, 0), company3));
			add(new Employee("Benjamin Franklin", new Position("Segítő", null, 400), 300, LocalDateTime.of(2019, 1, 1, 0, 0), company3));
			add(new Employee("Charles Patoshik", new Position("Naplopó", Education.NONE, 100), 100, LocalDateTime.of(2017, 1, 1, 0, 0), company3));
			add(new Employee("David Apolskis", new Position("Naplopó", Education.NONE, 100), 50, LocalDateTime.of(2022, 5, 1, 0, 0), company3));
			
			// Company 5
			add(new Employee("Sanyi", new Position("Vezér", Education.PHD, 3800), 3500, LocalDateTime.of(2015, 1, 1, 0, 0), company5));
			
			// No company
			add(new Employee("Tom", new Position("Naplopó", Education.NONE, 100), 100, LocalDateTime.of(2017, 1, 1, 0, 0)));
			add(new Employee("Jerry", new Position("Naplopó", Education.NONE, 100), 100, LocalDateTime.of(2017, 1, 1, 0, 0)));
		}};
		
		employeeRepository.saveAll(employees);
	}
	
	@Autowired
	private CompanyService companyService;
	
	public void runRepositoryMethods() {
		List<Company> companiesWithHighSalaryEmployee = companyRepository.getCompaniesWithHighSalaryEmployee(3900);
		System.out.println("   Companies with more than 3900 salary employees: " + companiesWithHighSalaryEmployee.size());
		for (Company company : companiesWithHighSalaryEmployee) {
			System.out.println("      " + company);
		}
		
		List<Company> companiesWithEmployeesMoreThan = companyRepository.getCompaniesWithEmployeesMoreThan(2L);
		System.out.println("   Companies with more than 2 employees: " + companiesWithEmployeesMoreThan.size());
		for (Company company : companiesWithEmployeesMoreThan) {
			System.out.println("      " + company);
		}
		
		List<CompanysAverageSalaries> companysAverageSalaries = companyRepository.getCompanysAverageSalaries(3L);
		System.out.println("   Company with registration number '3' average salary data:");
		for (CompanysAverageSalaries companysAverageSalariesInner : companysAverageSalaries) {
			System.out.println("      " + companysAverageSalariesInner);
		}
		
		companyService.changeSalaryForPositionOfCompany("Segítő", 444, 3L);
	}
}
