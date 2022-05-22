package hu.webuni.hr.comtur.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.webuni.hr.comtur.model.Employee;

/**
 * Thymeleaf controller.
 * @author comtur
 */
@Controller
public class HrTlController {

	private List<Employee> employees = new ArrayList<>();
	
	{
		employees.add(new Employee(1L, "Attila", "Vezérigazgató", 1900, LocalDateTime.of(2010, 1, 1, 0, 0)));
		employees.add(new Employee(2L, "Edit", "HR-es", 1000, LocalDateTime.of(2018, 1, 1, 0, 0)));
		employees.add(new Employee(3L, "Mici", "Takarító", 800, LocalDateTime.of(2022, 1, 1, 0, 0)));
		employees.add(new Employee(4L, "Bandi", "Mindenes", 1234, LocalDateTime.of(2015, 1, 1, 0, 0)));
	}
	
	@GetMapping("/")
	public String home() {
		return "index";
	}
	
	@GetMapping("/employees")
	public String listEmployees(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		return "employees";
	}
	
	@PostMapping("/employees")
	public String addEmployee(Employee employee) {
		employees.add(employee);
		return "redirect:employees";
	}
}
