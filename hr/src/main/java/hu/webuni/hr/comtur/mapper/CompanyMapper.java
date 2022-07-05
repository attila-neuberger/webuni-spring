package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.comtur.dto.CompanyDto;
import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.model.Company;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	@Mapping(target = "companyType", source = "companyType.name")
	CompanyDto companyToDto(Company company);
	
	List<CompanyDto> companiesToDtos(List<Company> companies);
	
	@Named("summary")
	@Mapping(target = "employees", ignore = true)
	@Mapping(target = "companyType", source = "companyType.name")
	CompanyDto companyToDtoWithNoEmployees(Company company);

	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companiesToDtosWithNoEmployees(List<Company> companies);

	@InheritInverseConfiguration(name = "companyToDto")
	Company dtoToCompany(CompanyDto companyDto);
	
	List<Company> dtosToCompanies(List<CompanyDto> companyDtos);
	
	/* ***** Mappings of nested objects: ***** */
	
	@Mapping(target = "title", source = "position")
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "supervisor", ignore = true)
	@Mapping(target = "subordinates", ignore = true)
	EmployeeDto employeeToDtoWithNoCompany(Employee employee);

	List<EmployeeDto> employeesToDtosWithNoCompany(List<Employee> employees);

	@Mapping(target = "company", ignore = true)
	@Mapping(target = "supervisor", ignore = true)
	@Mapping(target = "subordinates", ignore = true)
	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	
	PositionDto positionToDto(Position position);
	
	@Mapping(target = "employees", ignore = true)
	Position dtoToPosition(PositionDto positionDto);
}
