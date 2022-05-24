package hu.webuni.hr.comtur.dto;

import java.time.LocalDateTime;

import hu.webuni.hr.comtur.model.Employee;

public class EmployeeDto extends Employee implements IDtoKey {

	public EmployeeDto() {
		super();
	}

	public EmployeeDto(long id, String name, String position, int salary, LocalDateTime startDate) {
		super(id, name, position, salary, startDate);
	}
}
