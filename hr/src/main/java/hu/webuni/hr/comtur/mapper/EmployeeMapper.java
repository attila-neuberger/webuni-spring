package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	@Mapping(target = "title", source = "position")
	@Mapping(target = "company.employees", ignore = true)
	@Mapping(target = "company.companyType", source = "company.companyType.name")
	@Mapping(target = "supervisor", ignore = true)
	@Mapping(target = "subordinates", ignore = true)
	EmployeeDto employeeToDto(Employee employee);
	
	@Named("summary")
	@Mapping(target = "title", source = "position")
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "supervisor", ignore = true)
	@Mapping(target = "subordinates", ignore = true)
	EmployeeDto employeeToDtoWithNoCompany(Employee employee);

	@IterableMapping(qualifiedByName = "summary")
	List<EmployeeDto> employeesToDtosWithNoCompany(List<Employee> employees);

	@Named("summary")
	@Mapping(target = "company", ignore = true)
	@InheritInverseConfiguration(name = "employeeToDtoWithNoCompany")
	Employee dtoToEmployeeWithNoCompany(EmployeeDto employeeDto);
	
	@InheritInverseConfiguration(name = "employeeToDto")
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	@IterableMapping(qualifiedByName = "summary")
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	
	/* ***** Mappings of nested objects: ***** */
	
	PositionDto positionToDto(Position position);
	
	@Mapping(target = "employees", ignore = true)
	Position dtoToPosition(PositionDto positionDto);
}
