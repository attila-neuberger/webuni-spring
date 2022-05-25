package hu.webuni.hr.comtur.web;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.CompanyDto;
import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.VisibleView;

@RestController
@RequestMapping("/api/companies")
public class HrRestCompanyController extends HrBaseRestController<CompanyDto> {
	
	{
		List<EmployeeDto> employeesOfCompany1 = new ArrayList<>(2);
		employeesOfCompany1.add(new EmployeeDto(8L, "Bea", "Szoftverfejlesztő", 1100, LocalDateTime.of(2018, 10, 20, 0, 0)));
		employeesOfCompany1.add(new EmployeeDto(9L, "Ákos", "Szoftverfejlesztő", 1010, LocalDateTime.of(2021, 12, 1, 0, 0)));
		entities.put(1L, new CompanyDto(1L, "Cég 1", "1111 Bp, Fő u. 1.", employeesOfCompany1));
		entities.put(2L, new CompanyDto(2L, "Cég 2", "2112 Veresegyház, Mellék u. 2.", null));
	}
	
	@Override
	@GetMapping
	@JsonView(VisibleView.class)
	public Collection<CompanyDto> getAll() {
		return getAllCompaniewWithoutEmployees();
	}

	private Collection<CompanyDto> getAllCompaniewWithoutEmployees() {
		Collection<CompanyDto> result = new ArrayList<>(entities.size());
		for (CompanyDto company : entities.values()) {
			result.add(new CompanyDto(company.getCompanyRegistrationNumber(), company.getName(), company.getAddress(), null));
		}
		return result;
	}
	
	@GetMapping(params = "full=true")
	public Collection<CompanyDto> getAllWithEmployees() {
		return entities.values();
	}
	
	@GetMapping(params = "full=false")
	@JsonView(VisibleView.class)
	public Collection<CompanyDto> getAllWithoutEmployees() {
		return getAllCompaniewWithoutEmployees();
	}
	
	@Override
	@GetMapping("/{id}")
	@JsonView(VisibleView.class)
	public ResponseEntity<CompanyDto> getById(@PathVariable long id) {
		return getCompanyWithoutEmployees(id);
	}

	private ResponseEntity<CompanyDto> getCompanyWithoutEmployees(long id) {
		CompanyDto entity = entities.get(id);
		if (entity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new CompanyDto(entity.getId(), entity.getName(), entity.getAddress(), null));
	}
	
	@GetMapping(value = "/{id}", params = "full=true")
	public ResponseEntity<CompanyDto> getByIdWithEmployees(@PathVariable long id) {
		CompanyDto entity = entities.get(id);
		if (entity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(entity);
	}
	
	@GetMapping(value = "/{id}", params = "full=false")
	@JsonView(VisibleView.class)
	public ResponseEntity<CompanyDto> getByIdWithoutEmployees(@PathVariable long id) {
		CompanyDto entity = entities.get(id);
		if (entity == null) {
			return ResponseEntity.notFound().build();
		}
		return getCompanyWithoutEmployees(id);
	}
	
	@PostMapping("/{companyId}/add")
	public ResponseEntity<CompanyDto> createEmployeeForCompany(@RequestBody EmployeeDto employee, @PathVariable long companyId) {
		if (!entities.containsKey(companyId)) {
			return ResponseEntity.notFound().build();
		}
		CompanyDto companyDto = entities.get(companyId);
		if (companyDto.getEmployees() == null) {
			List<EmployeeDto> employeeList = new ArrayList<>(1);
			employeeList.add(employee);
			companyDto.setEmployees(employeeList);
		} else {
			companyDto.getEmployees().add(employee);
		}
		return ResponseEntity.ok(companyDto);
	}
	
	@DeleteMapping("/{companyId}/delete/{employeeId}")
	public ResponseEntity<CompanyDto> deleteEmployeeFromCompany(@PathVariable long companyId, @PathVariable long employeeId) {
		if (!entities.containsKey(companyId)) {
			return ResponseEntity.notFound().build();
		}
		CompanyDto companyDto = entities.get(companyId);
		if (companyDto.getEmployees() == null) {
			return ResponseEntity.notFound().build();
		}
		boolean found = false;
		for (int i = 0; i < companyDto.getEmployees().size() && !found; ++i) {
			if (companyDto.getEmployees().get(i).getId() == employeeId) {
				companyDto.getEmployees().remove(i);
				found = true;
			}
		}
		if (!found) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(companyDto);
	}
	
	@PutMapping("/{companyId}/swap")
	public ResponseEntity<CompanyDto> swapEmployeesOfCompany(@RequestBody List<EmployeeDto> employees, @PathVariable long companyId) {
		if (!entities.containsKey(companyId)) {
			return ResponseEntity.notFound().build();
		}
		CompanyDto companyDto = entities.get(companyId);
		companyDto.setEmployees(employees);
		return ResponseEntity.ok(companyDto);
	}
}
