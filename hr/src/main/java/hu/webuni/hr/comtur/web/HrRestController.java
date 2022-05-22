package hu.webuni.hr.comtur.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.comtur.dto.EmployeeDto;

@RestController
@RequestMapping("/api/employees")
public class HrRestController {
	
	private Map<Long, EmployeeDto> employees;
	
	{
		employees = new HashMap<>();
		employees.put(1L, new EmployeeDto(1L, "Attila", "Vezérigazgató", 1900, LocalDateTime.of(2010, 1, 1, 0, 0)));
		employees.put(2L, new EmployeeDto(2L, "Edit", "HR-es", 1000, LocalDateTime.of(2018, 1, 1, 0, 0)));
		employees.put(3L, new EmployeeDto(3L, "Mici", "Takarító", 800, LocalDateTime.of(2022, 1, 1, 0, 0)));
		employees.put(4L, new EmployeeDto(4L, "Bandi", "Mindenes", 1234, LocalDateTime.of(2015, 1, 1, 0, 0)));
	}
	
	@GetMapping
	public Collection<EmployeeDto> getAll() {
		return employees.values();
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> getById(@PathVariable long id) {
		EmployeeDto employee = employees.get(id);
		if (employee == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(employee);
	}
	
	@PostMapping
	public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeDto employee) {
		if (employees.containsKey(employee.getId())) {
			return ResponseEntity.unprocessableEntity().build();
		}
		employees.put(employee.getId(), employee);
		return ResponseEntity.ok(employee);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> modifyEmployee(@PathVariable long id, @RequestBody EmployeeDto employee) {
		if (!employees.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}
		employee.setId(id);
		employees.put(id, employee);
		return ResponseEntity.ok(employee);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<EmployeeDto> deleteEmployee(@PathVariable long id) {
		if (employees.containsKey(id)) {
			employees.remove(id);
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@GetMapping("/salaryThreshold={salary}")
	public Collection<EmployeeDto> getEmployeesAboveSalary(@PathVariable int salary) {
		System.out.println("Salary threshold: " + salary);
		Collection<EmployeeDto> result = new ArrayList<>();
		for (EmployeeDto employee : employees.values()) {
			if (employee.getSalary() >= salary) {
				result.add(employee);
			}
		}
		return result;
	}
}
