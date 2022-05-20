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
		if (days > hrConfigProperties.getEmployeesalary().getSmart().getLimit()[0] * DAYS_IN_YEAR) {
			return hrConfigProperties.getEmployeesalary().getSmart().getPercent()[0];
		} else if (days > hrConfigProperties.getEmployeesalary().getSmart().getLimit()[1] * DAYS_IN_YEAR) {
			return hrConfigProperties.getEmployeesalary().getSmart().getPercent()[1];
		} else if (days > hrConfigProperties.getEmployeesalary().getSmart().getLimit()[2] * DAYS_IN_YEAR) {
			return hrConfigProperties.getEmployeesalary().getSmart().getPercent()[2];
		} else {
			return 0;
		}
	}
}
