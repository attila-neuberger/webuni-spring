package hu.webuni.hr.comtur.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Employee;

/**
 * Sets employee's new salary.
 * 
 * @author comtur
 */
@Service
public class SalaryService {

	@Autowired
	private IEmployeeService iEmployeeService;
	
	public void setNewSalary(Employee employee) {
		employee.setSalary((int)(employee.getSalary() / 100.0 * (100 + iEmployeeService.getPayRaisePercent(employee))));
	}
}
