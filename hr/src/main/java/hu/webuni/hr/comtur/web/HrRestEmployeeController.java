package hu.webuni.hr.comtur.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class HrRestEmployeeController extends HrBaseRestController<EmployeeDto> {
	
	@Autowired
	private EmployeeService employeeService;
	
	{
		entities.put(1L, new EmployeeDto(1L, "Attila", "Vezérigazgató", 1900, LocalDateTime.of(2010, 1, 1, 0, 0)));
		entities.put(2L, new EmployeeDto(2L, "Edit", "HR-es", 1000, LocalDateTime.of(2018, 1, 1, 0, 0)));
		entities.put(3L, new EmployeeDto(3L, "Mici", "Takarító", 800, LocalDateTime.of(2022, 1, 1, 0, 0)));
		entities.put(4L, new EmployeeDto(4L, "Bandi", "Mindenes", 1234, LocalDateTime.of(2015, 1, 1, 0, 0)));
	}
	
	@GetMapping(params = "salaryThreshold")
	public Collection<EmployeeDto> getEmployeesAboveSalary(@RequestParam int salaryThreshold) {
		System.out.println("Salary threshold: " + salaryThreshold);
		Collection<EmployeeDto> result = new ArrayList<>();
		for (EmployeeDto employee : entities.values()) {
			if (employee.getSalary() >= salaryThreshold) {
				result.add(employee);
			}
		}
		return result;
	}
	
	@PostMapping("/raise")
	public int getPayRaisePercent(@RequestBody EmployeeDto employee) {
		return employeeService.getPayRaisePercent(employee);
	}
}
