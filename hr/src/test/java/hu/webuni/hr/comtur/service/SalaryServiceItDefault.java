package hu.webuni.hr.comtur.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import hu.webuni.hr.comtur.model.Education;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;

@SpringBootTest
@ActiveProfiles("test")
public class SalaryServiceItDefault {

	@Autowired
	SalaryService salaryService;
	
	Employee employee1 = new Employee(1L, "Name", new Position("Position", Education.NONE), 
			1000, LocalDateTime.of(2022, 1, 1, 0, 0));
	Employee employee2 = new Employee(2L, "Name", new Position("Position", Education.NONE), 
			1000, LocalDateTime.of(2019, 1, 1, 0, 0));
	Employee employee3 = new Employee(3L, "Name", new Position("Position", Education.NONE), 
			1000, LocalDateTime.of(2010, 1, 1, 0, 0));
	
	@Test
	void testSetNewSalary() throws Exception {
		salaryService.setNewSalary(employee1);
		salaryService.setNewSalary(employee2);
		salaryService.setNewSalary(employee3);
		assertThat(employee1.getSalary()).isEqualTo(1050);
		assertThat(employee2.getSalary()).isEqualTo(1050);
		assertThat(employee3.getSalary()).isEqualTo(1050);
	}
}
