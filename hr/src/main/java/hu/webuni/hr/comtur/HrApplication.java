package hu.webuni.hr.comtur;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	private SalaryService salaryService;
	
	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Employee employee1 = new Employee(1L, "Name1", "Position1", 1000,
				LocalDateTime.of(2010, 1, 1, 0, 0));
		Employee employee2 = new Employee(2L, "Name2", "Position2", 1000,
				LocalDateTime.of(2018, 1, 1, 0, 0));
		Employee employee3 = new Employee(3L, "Name3", "Position3", 1000,
				LocalDateTime.of(2022, 1, 1, 0, 0));
		raiseSalary(employee1);
		raiseSalary(employee2);
		raiseSalary(employee3);
	}
	
	private void raiseSalary(Employee employee) {
		salaryService.setNewSalary(employee);
		System.out.println(String.format("%s's new salary: %d", employee.getName(),
				employee.getSalary()));
	}
}
