package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	public List<Employee> findBySalaryGreaterThan(int salaryThreshold) {
		return ((EmployeeRepository)repository).findBySalaryGreaterThan(salaryThreshold);
	}
	
	public List<Employee> findByPosition(String position, Pageable pageable) {
		Page<Employee> page = ((EmployeeRepository)repository).findByPositionName(position, pageable);
		System.out.println("Page object of findByPosition:");
		System.out.println("   page.getNumber(): " + page.getNumber());
		System.out.println("   page.getNumberOfElements(): " + page.getNumberOfElements());
		System.out.println("   page.getSize(): " + page.getSize());
		System.out.println("   page.getTotalElements(): " + page.getTotalElements());
		System.out.println("   page.getTotalPages(): " + page.getTotalPages());
		System.out.println("   page.getSort(): " + page.getSort());
		System.out.println("   page.isFirst(): " + page.isFirst());
		return page.getContent();
	}
	
	public List<Employee> findByNameStartingWith(String name) {
		return ((EmployeeRepository)repository).findByNameIgnoreCaseStartingWith(name);
	}
	
	public List<Employee> findByStartDateBetween(LocalDateTime startDateFrom, LocalDateTime startDateTo) {
		return ((EmployeeRepository)repository).findByStartDateBetween(startDateFrom, startDateTo);
	}
}
