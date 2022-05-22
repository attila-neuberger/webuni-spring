package hu.webuni.hr.comtur.service;

import java.time.Duration;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.config.HrConfigProperties;
import hu.webuni.hr.comtur.model.Employee;

@Service
public class SmartEmployeeService implements EmployeeService {
	
	private final static int DAYS_IN_YEAR = 365;
	
	@Autowired
	HrConfigProperties hrConfigProperties;

	@Override
	public int getPayRaisePercent(Employee employee) {
		Duration duration = Duration.between(employee.getStartDate(), LocalDateTime.now());
		long days = duration.toDays();
		
		// We assume array values are sorted in ascending order and limit and percent indices match each other.
		for (int i = 0; i < hrConfigProperties.getEmployeesalary().getSmart().getLimit().length; ++i) {
			if (days > hrConfigProperties.getEmployeesalary().getSmart().getLimit()[i] * DAYS_IN_YEAR) {
				return hrConfigProperties.getEmployeesalary().getSmart().getPercent()[i];
			}
		}
		return 0;
	}
}
