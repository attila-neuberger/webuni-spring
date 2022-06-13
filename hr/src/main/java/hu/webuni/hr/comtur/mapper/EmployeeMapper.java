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
	EmployeeDto employeeToDto(Employee employee);
	
	@Named("summary")
	@Mapping(target = "title", source = "position")
	@Mapping(target = "company", ignore = true)
	EmployeeDto employeeToDtoWithNoCompany(Employee employee);

	@IterableMapping(qualifiedByName = "summary")
	List<EmployeeDto> employeesToDtosWithNoCompany(List<Employee> employees);

	@Mapping(target = "company", ignore = true)
	@InheritInverseConfiguration(name = "employeeToDtoWithNoCompany")
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	
	/* ***** Mappings of nested objects: ***** */
	
	PositionDto positionToDto(Position position);
	
	Position dtoToPosition(PositionDto positionDto);
}
