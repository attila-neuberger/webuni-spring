package hu.webuni.hr.comtur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.config.HrConfigProperties;
import hu.webuni.hr.comtur.model.Employee;

@Service
public class DefaultEmployeeService implements EmployeeService {
	
	@Autowired
	HrConfigProperties hrConfigProperties;

	@Override
	public int getPayRaisePercent(Employee employee) {
		return hrConfigProperties.getEmployeesalary().getDef().getPercent();
	}
}
