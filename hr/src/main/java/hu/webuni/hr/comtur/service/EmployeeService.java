package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.repository.EmployeeRepository;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public abstract class EmployeeService extends BaseService<Employee> implements IEmployeeService {
	
	public List<Employee> findByPosition(String position) {
		return ((EmployeeRepository)repository).findByPosition(position);
	}
	
	public List<Employee> findByNameStartingWith(String name) {
		return ((EmployeeRepository)repository).findByNameIgnoreCaseStartingWith(name);
	}
	
	public List<Employee> findByStartDateBetween(LocalDateTime startDateFrom, LocalDateTime startDateTo) {
		return ((EmployeeRepository)repository).findByStartDateBetween(startDateFrom, startDateTo);
	}
}
