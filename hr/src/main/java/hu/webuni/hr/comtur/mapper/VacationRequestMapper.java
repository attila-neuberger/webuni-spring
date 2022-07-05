package hu.webuni.hr.comtur.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.webuni.hr.comtur.dto.EmployeeDto;
import hu.webuni.hr.comtur.dto.PositionDto;
import hu.webuni.hr.comtur.dto.VacationRequestDto;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.model.VacationRequest;

@Mapper(componentModel = "spring")
public interface VacationRequestMapper {

	@Mapping(target = "start", source = "vacationStart")
	@Mapping(target = "end", source = "vacationEnd")
	VacationRequestDto vacationRequestToDto(VacationRequest vacationRequest);
	
	List<VacationRequestDto> vacationRequestsToDtos(List<VacationRequest> vacationRequests);
	
	@InheritInverseConfiguration
	VacationRequest dtoToVacationRequest(VacationRequestDto vacationRequestDto);
	
	List<VacationRequest> dtosToVacationRequests(List<VacationRequestDto> vacationRequestDtos);
	
	/* ***** Mappings of nested objects: ***** */
	
	@Mapping(target = "title", source = "position")
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "manager", ignore = true)
	@Mapping(target = "subordinates", ignore = true)
	EmployeeDto employeeToDtoWithNoCompany(Employee employee);

	List<EmployeeDto> employeesToDtosWithNoCompany(List<Employee> employees);

	@Mapping(target = "company", ignore = true)
	@Mapping(target = "manager", ignore = true)
	@Mapping(target = "subordinates", ignore = true)
	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);
	
	List<Employee> dtosToEmployees(List<EmployeeDto> employeeDtos);
	
	PositionDto positionToDto(Position position);
	
	@Mapping(target = "employees", ignore = true)
	Position dtoToPosition(PositionDto positionDto);
}
