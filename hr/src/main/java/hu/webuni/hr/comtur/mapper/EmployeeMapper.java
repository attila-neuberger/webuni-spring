package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	@Mapping(target = "title", source = "position")
	@Mapping(target = "companyDto", source = "company")
	EmployeeDto employeeToDto(Employee employee);
	
	@Named("summary")
	@Mapping(target = "title", source = "position")
	@Mapping(target = "companyDto", ignore = true)
	EmployeeDto employeeToDtoWithNoCompany(Employee employee);

	@IterableMapping(qualifiedByName = "summary")
	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@Mapping(target = "company", ignore = true)
	@InheritInverseConfiguration(name = "employeeToDto")
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
