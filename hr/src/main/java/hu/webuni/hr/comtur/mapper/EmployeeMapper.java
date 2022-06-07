package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	@Mapping(source = "company", target = "companyDto")
	EmployeeDto employeeToDto(Employee employee);

	List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@Mapping(source = "companyDto", target = "company")
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
}
