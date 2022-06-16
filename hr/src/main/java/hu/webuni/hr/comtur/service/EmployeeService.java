package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.repository.EmployeeRepository;
import hu.webuni.hr.comtur.repository.PositionRepository;

/**
 * Methods are inherited from {@link BaseService}.
 * 
 * @author comtur
 */
@Service
public abstract class EmployeeService extends BaseService<Employee> implements IEmployeeService {
	
	/**
	 * Additional repository for positions.
	 */
	@Autowired
	PositionRepository positionRepository;
	
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
		System.out.println("   page.isLast(): " + page.isLast());
		return page.getContent();
	}
	
	public List<Employee> findByNameStartingWith(String name) {
		return ((EmployeeRepository)repository).findByNameIgnoreCaseStartingWith(name);
	}
	
	public List<Employee> findByStartDateBetween(LocalDateTime startDateFrom, LocalDateTime startDateTo) {
		return ((EmployeeRepository)repository).findByStartDateBetween(startDateFrom, startDateTo);
	}

	@Override
	public Employee save(Employee employee) {
		savePosition(employee);
		return super.save(employee);
	}
	
	private void savePosition(Employee employee) {
		Position transientPosition = employee.getPosition();
		if (transientPosition != null && transientPosition.getName() != null && !transientPosition.getName().isEmpty()) {
			Optional<Position> optionalPosition = positionRepository.findByName(transientPosition.getName());
			if (optionalPosition.isPresent()) {
				employee.setPosition(optionalPosition.get());
			} else {
				employee.setPosition(positionRepository.save(transientPosition));
			}
		} else {
			employee.setPosition(null);
		}
	}
	
	public List<Employee> saveAll(Iterable<Employee> employees) {
		for (Employee employee : employees) {
			savePosition(employee);
		}
		return ((EmployeeRepository)repository).saveAll(employees);
	}
	
	public EmployeeRepository getRepository() {
		return (EmployeeRepository)super.getRepository();
	}
}
