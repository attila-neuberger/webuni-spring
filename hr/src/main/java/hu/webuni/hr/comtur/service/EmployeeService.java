package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;

import hu.webuni.hr.comtur.model.Employee;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
public abstract class EmployeeService extends BaseService<Employee> implements IEmployeeService {
	
	{
		entities.put(1L, new Employee(1L, "Attila", "Vezérigazgató", 1900, LocalDateTime.of(2010, 1, 1, 0, 0)));
		entities.put(2L, new Employee(2L, "Edit", "HR-es", 1000, LocalDateTime.of(2018, 1, 1, 0, 0)));
		entities.put(3L, new Employee(3L, "Mici", "Takarító", 800, LocalDateTime.of(2022, 1, 1, 0, 0)));
		entities.put(4L, new Employee(4L, "Bandi", "Mindenes", 1234, LocalDateTime.of(2015, 1, 1, 0, 0)));
	}
}
