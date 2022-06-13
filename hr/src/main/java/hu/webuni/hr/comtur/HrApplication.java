package hu.webuni.hr.comtur;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.comtur.model.Education;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.service.InitDbService;
import hu.webuni.hr.comtur.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	private SalaryService salaryService;
	
	@Autowired
	private InitDbService initDbService;
	
	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Employee employee1 = new Employee(1L, "Name1", new Position("Position1", Education.NONE, 500), 1000,
				LocalDateTime.of(2010, 1, 1, 0, 0));
		Employee employee2 = new Employee(2L, "Name2", new Position("Position2", Education.GRADUATION, 800), 1000,
				LocalDateTime.of(2018, 1, 1, 0, 0));
		Employee employee3 = new Employee(3L, "Name3", new Position("Position3", Education.GRADUATION, 800), 1000,
				LocalDateTime.of(2022, 1, 1, 0, 0));
		Employee employee4 = new Employee(4L, "Name4", new Position("Position4", Education.UNIVERSITY, 1200), 1234,
				LocalDateTime.of(2015, 1, 1, 0, 0));
		raiseSalary(employee1);
		raiseSalary(employee2);
		raiseSalary(employee3);
		raiseSalary(employee4);
		
		initDbService.clearDatabase();
		initDbService.insertTestData();
		initDbService.runRepositoryMethods();
	}
	
	private void raiseSalary(Employee employee) {
		salaryService.setNewSalary(employee);
		System.out.println(String.format("%s's new salary: %d", employee.getName(),
				employee.getSalary()));
	}
}
