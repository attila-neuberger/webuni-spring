package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	EmployeeDto employeeToDto(Employee employee);

	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
