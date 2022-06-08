package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	// @Mapping(target = "companyDto", source = "company")
	@Mapping(target = "title", source = "position")
	EmployeeDto employeeToDto(Employee employee);

	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@Mapping(target = "company", ignore = true)
	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
