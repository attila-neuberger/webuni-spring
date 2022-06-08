package hu.webuni.hr.comtur.web;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.mapper.EmployeeMapper;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.service.EmployeeService;
import hu.webuni.hr.comtur.service.exception.EmployeeException;

/**
 * Employee controller. Methods use exception throwing.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/employees")
public class HrRestEmployeeController {
	
	@Autowired
	EmployeeService employeeService;
	
	@Autowired
	EmployeeMapper employeeMapper;
	
	@GetMapping
	public Collection<EmployeeDto> getAll() {
		return employeeMapper.employeesToDtos(employeeService.findAll());
	}

	@GetMapping("/{id}")
	public EmployeeDto getById(@PathVariable long id) {
		Employee employee = employeeService.findById(id).orElseThrow();
		if (employee == null) {
			throw new EmployeeException(String.format("Employee with ID '%d' does not exist.", id));
		}
		return employeeMapper.employeeToDto(employee);
	}
	
	@PostMapping
	public EmployeeDto create(@RequestBody @Valid EmployeeDto employeeDto) {
		if (employeeService.exists(employeeDto.getId())) {
			throw new EmployeeException(String.format("Employee with ID '%d' already exists.", employeeDto.getId()));
		}
		Employee employee = employeeService.save(employeeMapper.dtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}
	
	@PutMapping("/{id}")
	public EmployeeDto modify(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {
		if (!employeeService.exists(id)) {
			throw new EmployeeException(String.format("Employee with ID '%d' does not exist.", id));
		}
		employeeDto.setId(id);
		Employee employee = employeeService.save(employeeMapper.dtoToEmployee(employeeDto));
		return employeeMapper.employeeToDto(employee);
	}
	
	@DeleteMapping("/{id}")
	public void remove(@PathVariable long id) {
		if (employeeService.exists(id)) {
			employeeService.delete(id);
		} else {
			throw new EmployeeException(String.format("Employee with ID '%d' does not exist.", id));
		}
	}
	
	@GetMapping(params = "salaryThreshold")
	public Collection<EmployeeDto> getEmployeesAboveSalary(@RequestParam int salaryThreshold) {
		System.out.println("Employee REST get with salary threshold: " + salaryThreshold);
		/*Collection<EmployeeDto> result = new ArrayList<>();
		for (Employee employee : employeeService.findAll()) {
			if (employee.getSalary() >= salaryThreshold) {
				result.add(employeeMapper.employeeToDto(employee));
			}
		}
		return result;*/
		return employeeMapper.employeesToDtos(employeeService.findBySalaryGreaterThan(salaryThreshold));
	}
	
	@PostMapping("/raise")
	public int getPayRaisePercent(@RequestBody Employee employee) {
		return employeeService.getPayRaisePercent(employee);
	}
	
	@GetMapping(params = "position")
	public Collection<EmployeeDto> getEmployeesWithPosition(@RequestParam String position) {
		System.out.println("Employee REST get position: " + position);
		return employeeMapper.employeesToDtos(employeeService.findByPosition(position));
	}
	
	@GetMapping(params = "name")
	public Collection<EmployeeDto> getEmployeesWithNameStartingWith(@RequestParam String name) {
		System.out.println("Employee REST get name starting with (ignore case): " + name);
		return employeeMapper.employeesToDtos(employeeService.findByNameStartingWith(name));
	}
	
	@GetMapping(params = {"startDateFrom", "startDateTo"})
	public Collection<EmployeeDto> getEmployeesWithStartDateBetween(@RequestParam LocalDateTime startDateFrom,
			@RequestParam LocalDateTime startDateTo) {
		DateFormat df = new SimpleDateFormat("yyyy.MM.dd. HH:mm:ss");
		System.out.println("Employee REST get start date between: " + df.format(new Date(startDateFrom.toEpochSecond(ZoneOffset.UTC) * 1000))
				+ " - " + df.format(new Date(startDateTo.toEpochSecond(ZoneOffset.UTC) * 1000)));
		return employeeMapper.employeesToDtos(employeeService.findByStartDateBetween(startDateFrom, startDateTo));
	}
}
