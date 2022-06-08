package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.comtur.dto.CompanyDto;
import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	CompanyDto companyToDto(Company company);
	
	List<CompanyDto> companiesToDtos(List<Company> companies);
	
	@Named("summary")
	@Mapping(target = "employees", ignore = true)
	CompanyDto companyToDtoWithNoEmployees(Company company);

	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToDtosWithNoEmployees(List<Company> companies);

	Company dtoToCompany(CompanyDto companyDto);
	
	List<Company> dtosToCompanies(List<CompanyDto> companyDtos);
	
	/* ***** Mappings of nested objects: ***** */
	
	// @Mapping(target = "companyDto", source = "company")
	@Mapping(target = "title", source = "position")
	EmployeeDto employeeToDto(Employee employee);
	
	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@InheritInverseConfiguration
	@Mapping(target = "company", ignore = true)
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
