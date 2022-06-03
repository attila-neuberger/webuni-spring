package hu.webuni.hr.comtur.web;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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
import hu.webuni.hr.comtur.dto.Views;
import hu.webuni.hr.comtur.mapper.CompanyMapper;
import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.service.CompanyService;

/**
 * Company controller. Methods use ResponseEntity.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/companies")
public class HrRestCompanyController {
	
	CompanyService companyService = new CompanyService();
	
	@Autowired
	CompanyMapper companyMapper;
	
	@GetMapping
	@JsonView(Views.VisibleData.class)
	public Collection<CompanyDto> getAll() {
		Collection<CompanyDto> result = new ArrayList<>();
		for (Company company : companyService.findAll()) {
			result.add(new CompanyDto(company.getCompanyRegistrationNumber(), company.getName(), company.getAddress(), null));
		}
		return result;
	}

	@GetMapping(params = "full=true")
	public Collection<CompanyDto> getAllWithEmployees() {
		return companyMapper.companyToDtos(companyService.findAll());
	}
	
	@GetMapping("/{id}")
	@JsonView(Views.VisibleData.class)
	public ResponseEntity<CompanyDto> getById(@PathVariable long id) {
		CompanyDto entity = companyMapper.companyToDto(companyService.findById(id));
		if (entity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(new CompanyDto(entity.getId(), entity.getName(), entity.getAddress(), null));
	}

	@GetMapping(value = "/{id}", params = "full=true")
	public ResponseEntity<CompanyDto> getByIdWithEmployees(@PathVariable long id) {
		CompanyDto entity = companyMapper.companyToDto(companyService.findById(id));
		if (entity == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(entity);
	}
	
	@PostMapping
	public ResponseEntity<CompanyDto> create(@RequestBody @Valid CompanyDto companyDto) {
		if (companyService.containsKey(companyDto.getId())) {
			return ResponseEntity.unprocessableEntity().build();
		}
		Company company = companyService.save(companyMapper.dtoToCompany(companyDto));
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> modify(@PathVariable long id, @RequestBody @Valid CompanyDto companyDto) {
		if (!companyService.containsKey(id)) {
			return ResponseEntity.notFound().build();
		}
		companyDto.setId(id);
		Company company = companyService.save(companyMapper.dtoToCompany(companyDto));
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<CompanyDto> remove(@PathVariable long id) {
		if (companyService.containsKey(id)) {
			companyService.delete(id);
			return ResponseEntity.accepted().build();
		}
		return ResponseEntity.notFound().build();
	}
	
	@PostMapping("/{companyId}/add")
	public ResponseEntity<CompanyDto> createEmployeeForCompany(@RequestBody Employee employee, @PathVariable long companyId) {
		if (!companyService.containsKey(companyId)) {
			return ResponseEntity.notFound().build();
		}
		Company company = companyService.findById(companyId);
		if (company.getEmployees() == null) {
			List<Employee> employeeList = new ArrayList<>(1);
			employeeList.add(employee);
			company.setEmployees(employeeList);
		} else {
			company.getEmployees().add(employee);
		}
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@DeleteMapping("/{companyId}/delete/{employeeId}")
	public ResponseEntity<CompanyDto> deleteEmployeeFromCompany(@PathVariable long companyId, @PathVariable long employeeId) {
		if (!companyService.containsKey(companyId)) {
			return ResponseEntity.notFound().build();
		}
		Company company = companyService.findById(companyId);
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
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
	
	@PutMapping("/{companyId}/swap")
	public ResponseEntity<CompanyDto> swapEmployeesOfCompany(@RequestBody List<Employee> employees, @PathVariable long companyId) {
		if (!companyService.containsKey(companyId)) {
			return ResponseEntity.notFound().build();
		}
		Company company = companyService.findById(companyId);
		company.setEmployees(employees);
		return ResponseEntity.ok(companyMapper.companyToDto(company));
	}
}
