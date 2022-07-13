package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.CompanyType;
import hu.webuni.hr.comtur.model.Education;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.model.PositionXCompany;
import hu.webuni.hr.comtur.model.not_entity.CompanysAverageSalaries;
import hu.webuni.hr.comtur.repository.CompanyRepository;
import hu.webuni.hr.comtur.repository.CompanyTypeRepository;
import hu.webuni.hr.comtur.repository.EmployeeRepository;
import hu.webuni.hr.comtur.repository.PositionRepository;
import hu.webuni.hr.comtur.repository.PositionXCompanyRepository;
import hu.webuni.hr.comtur.repository.VacationRequestRepository;

@Service
public class InitDbService {
	
	/* ********** Services ********** */
	
	@Autowired
	private CompanyService companyService;
	
	@Autowired
	private EmployeeService employeeService;

	/* ********** Repositories ********** */
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Autowired
	private PositionRepository positionRepository;
	
	@Autowired
	private CompanyTypeRepository companyTypeRepository;
	
	@Autowired
	private PositionXCompanyRepository positionXCompanyRepository;
	
	@Autowired
	private VacationRequestRepository vacationRequestRepository;
	
	/* ********** Password encoder ********** */
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	/**
	 * Saved company reference for running repository methods.
	 */
	private Company testCompany;
	
	public void clearDatabase() {
		vacationRequestRepository.deleteAll();
		positionXCompanyRepository.deleteAll();
		employeeRepository.deleteAll();
		companyRepository.deleteAll();
		positionRepository.deleteAll();
		companyTypeRepository.deleteAll();
	}
	
	public void insertTestData() {
		Company company1 = new Company(1L, "Attila programozócége", new CompanyType("BT"), "2800 Tatabánya, Fő u. 1.");
		Company company2 = new Company(42L, "Stopposok", new CompanyType("ZRT"), "Föld bolygó");
		Company company3 = new Company(3L, "Fox River", new CompanyType("ZRT"), "USA, Chicago, Illinois");
		this.testCompany = company3; // Saving reference of this company, for testing.
		Company company4 = new Company(4L, "Holt költők társasága", new CompanyType("ZRT"), "ismeretlen");
		Company company5 = new Company(5L, "Pici cég", new CompanyType("KFT"), "2112 Veresegyház, Fő u. 1.");
		
		companyRepository.save(company1);
		companyRepository.save(company2);
		companyRepository.save(company3);
		companyRepository.save(company4);
		companyRepository.save(company5);
		
		@SuppressWarnings("serial")
		List<Employee> employees = new ArrayList<>() {{
			
			// Company 1
			Employee employee1 = new Employee("Attila", new Position("Vezér", Education.UNIVERSITY), 4000, LocalDateTime.of(2010, 1, 1, 0, 0), company1,
					"u_attila", passwordEncoder.encode("pass"), null);
			add(employee1);
			add(new Employee("Edit", new Position("Segítő", Education.GRADUATION), 2800, LocalDateTime.of(2018, 1, 1, 0, 0), company1,
					"u_edit", passwordEncoder.encode("pass"), employee1));
			add(new Employee("Mici", new Position("Naplopó", Education.NONE), 1000, LocalDateTime.of(2022, 1, 1, 0, 0), company1,
					"u_mici", passwordEncoder.encode("pass"), employee1));
			
			// Company 2
			Position positionS2 = new Position("Segítő", Education.GRADUATION);
			Employee employee2 = new Employee("Zaphod Beeblebrox", new Position("Vezér", Education.COLLEGE), 50_000_000, LocalDateTime.of(2005, 1, 1, 0, 0), company2,
					"u_zaphod", passwordEncoder.encode("pass"), null);
			add(employee2);
			add(new Employee("Arthur Dent", new Position("Naplopó", Education.NONE), 100, LocalDateTime.of(2020, 1, 1, 0, 0), company2,
					"u_arthur", passwordEncoder.encode("pass"), employee2));
			add(new Employee("Ford Prefect", positionS2, 10_000, LocalDateTime.of(2015, 1, 1, 0, 0), company2,
					"u_ford", passwordEncoder.encode("pass"), employee2));
			add(new Employee("Trillian", positionS2, 11_000, LocalDateTime.of(2018, 1, 1, 0, 0), company2,
					"u_trillian", passwordEncoder.encode("pass"), employee2));
			
			// Company 3
			Position positionS3 = new Position("Segítő", null);
			Position positionN3 = new Position("Naplopó", Education.NONE);
			Employee employee3 = new Employee("John Abruzzi", new Position("Vezér", Education.COLLEGE), 550, LocalDateTime.of(2014, 1, 1, 0, 0), company3,
					"u_abruzzi", passwordEncoder.encode("pass"), null);
			add(employee3);
			Employee employee4 = new Employee("Michael Scofield", positionS3, 500, LocalDateTime.of(2022, 1, 1, 0, 0), company3,
					"u_scofield", passwordEncoder.encode("pass"), employee3);
			add(employee4);
			add(new Employee("Lincoln Burrows", positionN3, 400, LocalDateTime.of(2018, 1, 1, 0, 0), company3,
					"u_burrows", passwordEncoder.encode("pass"), employee4)); // Burrows' manager is Scofield and not Abruzzi.
			add(new Employee("Charles Westmoreland", positionS3, 300, LocalDateTime.of(2005, 1, 1, 0, 0), company3,
					"u_westmoreland", passwordEncoder.encode("pass"), employee3));
			add(new Employee("Theodore Bagwell", positionS3, 400, LocalDateTime.of(2016, 1, 1, 0, 0), company3,
					"u_bagwell", passwordEncoder.encode("pass"), employee3));
			add(new Employee("Benjamin Franklin", positionS3, 300, LocalDateTime.of(2019, 1, 1, 0, 0), company3,
					"u_franklin", passwordEncoder.encode("pass"), employee3));
			add(new Employee("Charles Patoshik", positionN3, 100, LocalDateTime.of(2017, 1, 1, 0, 0), company3,
					"u_patoshik", passwordEncoder.encode("pass"), employee3));
			add(new Employee("David Apolskis", positionN3, 50, LocalDateTime.of(2022, 5, 1, 0, 0), company3,
					"u_apolskis", passwordEncoder.encode("pass"), employee3));
			
			// Company 5
			add(new Employee("Sanyi", new Position("Vezér", Education.PHD), 3500, LocalDateTime.of(2015, 1, 1, 0, 0), company5,
					"u_sanyi", passwordEncoder.encode("pass"), null));
			
			// No company
			add(new Employee("Tom", new Position("Naplopó", Education.NONE), 100, LocalDateTime.of(2017, 1, 1, 0, 0), null,
					"u_tom", passwordEncoder.encode("pass"), null));
			add(new Employee("Jerry", new Position("Naplopó", Education.NONE), 100, LocalDateTime.of(2017, 1, 1, 0, 0), null,
					"u_jerry", passwordEncoder.encode("pass"), null));
		}};
		
		employeeService.saveAll(employees);
		
		@SuppressWarnings("serial")
		List<PositionXCompany> positionXCompanies = new ArrayList<>() {{
			
			add(new PositionXCompany(2500, employees.get(0).getPosition(), company1)); // Attila
			add(new PositionXCompany(1250, employees.get(1).getPosition(), company1)); // Edit
			add(new PositionXCompany(1000, employees.get(2).getPosition(), company1)); // Mici
			
			add(new PositionXCompany(100, employees.get(4).getPosition(), company2)); // Arthur
			add(new PositionXCompany(1250, employees.get(5).getPosition(), company2)); // Ford
			add(new PositionXCompany(10_000, employees.get(3).getPosition(), company2)); // Zaphod
			
			add(new PositionXCompany(400, employees.get(8).getPosition(), company3)); // Scofield
			add(new PositionXCompany(100, employees.get(9).getPosition(), company3)); // Burrows
			add(new PositionXCompany(1000, employees.get(7).getPosition(), company3)); // Abruzzi
			
			add(new PositionXCompany(3800, employees.get(15).getPosition(), company5)); // Sanyi
		}};
		
		positionXCompanyRepository.saveAll(positionXCompanies);
	}
	
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
		
		List<CompanysAverageSalaries> companysAverageSalaries = companyRepository.getCompanysAverageSalaries(testCompany.getId());
		System.out.println("   Company with ID '" + testCompany.getId() + "' average salary data:");
		for (CompanysAverageSalaries companysAverageSalariesInner : companysAverageSalaries) {
			System.out.println("      " + companysAverageSalariesInner);
		}
		
		companyService.changeSalaryForPositionOfCompany("Segítő", 444, testCompany.getId());
	}
}
