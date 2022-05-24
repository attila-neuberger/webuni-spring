package hu.webuni.hr.comtur.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CompanyDtoHideNavigation extends CompanyDto {
	
	public CompanyDtoHideNavigation() {
		super();
	}

	public CompanyDtoHideNavigation(long companyRegistrationNumber, String name, String address,
			List<EmployeeDto> employees) {
		super(companyRegistrationNumber, name, address, employees);
	}

	@JsonIgnore
	public List<EmployeeDto> getEmployees() {
		return super.getEmployees();
	}
}
