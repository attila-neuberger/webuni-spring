package hu.webuni.hr.comtur.web;

import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.CompanyDto;
import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.Views;
import hu.webuni.hr.comtur.mapper.CompanyMapper;
import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.service.CompanyService;

/**
 * Company controller. Methods use ResponseEntity.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/companies")
public class HrRestCompanyController {
	
	@Autowired
	CompanyService companyService;
	
	@Autowired
	CompanyMapper companyMapper;
	
	@GetMapping
	@JsonView(Views.VisibleData.class)
	public Collection<CompanyDto> getAll() {
		/*Collection<CompanyDto> result = new ArrayList<>();
		for (Company company : companyService.findAll()) {
			result.add(new CompanyDto(company.getId(), company.getCompanyRegistrationNumber(), company.getName(),
					company.getAddress(), null));
		}
		return result;*/ // Using mapper's without-mapping method.
		return companyMapper.companiesToDtosWithNoEmployees(companyService.findAll());
	}

	@GetMapping(params = "full=true")
	public Collection<CompanyDto> getAllWithEmployees() {
		return companyMapper.companiesToDtos(companyService.findAll());
	}
	
	@GetMapping("/{id}")
	@JsonView(Views.VisibleData.class)
	public ResponseEntity<CompanyDto> getById(@PathVariable long id) {
		CompanyDto company = companyMapper.companyToDtoWithNoEmployees(companyService.findById(id).orElseThrow(() -> 
				new ResponseStatusException(HttpStatus.NOT_FOUND)));
		/*return ResponseEntity.ok(new CompanyDto(company.getId(), company.getCompanyRegistrationNumber(), company.getName(),
				company.getAddress(), null));*/ // Using mapper's without-mapping method.
		return ResponseEntity.ok(company);
	}

	@GetMapping(value = "/{id}", params = "full=true")
	public ResponseEntity<CompanyDto> getByIdWithEmployees(@PathVariable long id) {
		CompanyDto company = companyMapper.companyToDto(companyService.findById(id).orElseThrow(() -> 
				new ResponseStatusException(HttpStatus.NOT_FOUND)));
		return ResponseEntity.ok(company);
	}
	
	@PostMapping
	public ResponseEntity<CompanyDto> create(@RequestBody @Valid CompanyDto companyDto) {
		if (companyService.exists(companyDto.getId())) {
			return ResponseEntity.unprocessableEntity().build();
		}
		Company company = companyService.save(companyMapper.dtoToCompany(companyDto));
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modify(@PathVariable long id, @RequestBody @Valid CompanyDto companyDto) {
		if (!companyService.exists(id)) {
			return ResponseEntity.notFound().build();
		}
		companyDto.setId(id);
		Company company = companyService.save(companyMapper.dtoToCompany(companyDto));
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<CompanyDto> remove(@PathVariable long id) {
		if (companyService.exists(id)) {
			companyService.delete(id);
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/{companyId}/add")
	public ResponseEntity<CompanyDto> createEmployeeForCompany(@RequestBody EmployeeDto employeeDto, @PathVariable long companyId) {
		/*if (!companyService.exists(companyId)) {
			return ResponseEntity.notFound().build();
		}
		Company company = companyService.findById(companyId).orElseThrow();
		if (company.getEmployees() == null) {
			List<Employee> employeeList = new ArrayList<>(1);
			employeeList.add(employee);
			company.setEmployees(employeeList);
		} else {
			company.getEmployees().add(employee);
		}
		return ResponseEntity.ok(companyMapper.companyToDto(company));*/ // Service provides these functions.
		try {
			Company company = companyService.createEmployeeForCompany(companyId, companyMapper.dtoToEmployee(employeeDto));
			return ResponseEntity.ok(companyMapper.companyToDto(company));
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@DeleteMapping("/{companyId}/delete/{employeeId}")
	public ResponseEntity<CompanyDto> deleteEmployeeFromCompany(@PathVariable long companyId, @PathVariable long employeeId) {
		/*if (!companyService.exists(companyId)) {
			return ResponseEntity.notFound().build();
		}
		Company company = companyService.findById(companyId).orElseThrow();
		if (company.getEmployees() == null) {
			return ResponseEntity.notFound().build();
		}
		boolean found = false;
		for (int i = 0; i < company.getEmployees().size() && !found; ++i) {
			if (company.getEmployees().get(i).getId() == employeeId) {
				company.getEmployees().remove(i);
				found = true;
			}
		}
		if (!found) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(companyMapper.companyToDto(company));*/ // Service provides these functions.
		try {
			Company company = companyService.deleteEmployeeFromCompany(companyId, employeeId);
			return ResponseEntity.ok(companyMapper.companyToDto(company));
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{companyId}/swap")
	public ResponseEntity<CompanyDto> swapEmployeesOfCompany(@RequestBody List<EmployeeDto> employeeDtos, @PathVariable long companyId) {
		/*if (!companyService.exists(companyId)) {
			return ResponseEntity.notFound().build();
		}
		Company company = companyService.findById(companyId).orElseThrow();
		company.setEmployees(employees);
		return ResponseEntity.ok(companyMapper.companyToDto(company));*/ // Service provides these functions.
		try {
			Company company = companyService.swapEmployeesOfCompany(companyMapper.dtosToEmployees(employeeDtos), companyId);
			return ResponseEntity.ok(companyMapper.companyToDto(company));
		} catch (NoSuchElementException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
