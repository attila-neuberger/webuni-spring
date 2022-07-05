package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Employee_;
import hu.webuni.hr.comtur.model.Position;
import hu.webuni.hr.comtur.repository.EmployeeRepository;
import hu.webuni.hr.comtur.repository.PositionRepository;
import hu.webuni.hr.comtur.service.specification.EmployeeSpecifications;

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
	
	public List<Employee> findByPosition(String position, @PageableDefault(sort = {"id"}) Pageable pageable) {
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
	@Transactional
	public Employee save(Employee employee) {
		savePosition(employee);
		return super.save(employee);
	}
	
	@Transactional(propagation = Propagation.MANDATORY)
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
	
	@Transactional
	public List<Employee> saveAll(Iterable<Employee> employees) {
		for (Employee employee : employees) {
			savePosition(employee);
		}
		return ((EmployeeRepository)repository).saveAll(employees);
	}
	
	public List<Employee> findEmployeesByExample(Employee example) {
		Specification<Employee> specification = Specification.where(null);
		if (example.getId() > 0) {
			specification = specification.and(EmployeeSpecifications.hasId(example.getId()));
		}
		if (StringUtils.hasText(example.getName())) {
			specification = specification.and(EmployeeSpecifications.hasName(example.getName()));
		}
		if (example.getPosition() != null) {
			if (StringUtils.hasText(example.getPosition().getName())) {
				specification = specification.and(EmployeeSpecifications.hasPositionName(example.getPosition().getName()));
			}
		}
		if (example.getSalary() > 0) {
			specification = specification.and(EmployeeSpecifications.hasSalary(example.getSalary()));
		}
		if (example.getStartDate() != null) {
			specification = specification.and(EmployeeSpecifications.hasStartDate(example.getStartDate()));
		}
		if (example.getCompany() != null) {
			if (StringUtils.hasText(example.getCompany().getName())) {
				specification = specification.and(EmployeeSpecifications.hasCompanyName(example.getCompany().getName()));
			}
		}
		return ((EmployeeRepository)repository).findAll(specification, Sort.by(Employee_.ID));
	}
	
	@Transactional
	public Employee create(Employee employee) throws IllegalArgumentException {
		if (exists(employee.getId())) {
			throw new IllegalArgumentException(String.format("Entity with ID %d already exists.", employee.getId()));
		}
		return save(employee);
	}
	
	@Transactional
	public Employee modify(long id, Employee employee) throws NoSuchElementException {
		if (!exists(id)) {
			throw new NoSuchElementException(String.format("Entity with ID %d does not exist.", id));
		}
		employee.setId(id);
		return save(employee);
	}
	
	@Transactional
	public void remove(long id) throws NoSuchElementException {
		if (exists(id)) {
			delete(id);
		} else {
			throw new NoSuchElementException(String.format("Entity with ID %d does not exist.", id));
		}
	}
	
	public Optional<Employee> getEmployeeWithSubordinates(long id) {
		return getRepository().getEmployeeWithSubordinates(id);
	}
	
	public EmployeeRepository getRepository() {
		return (EmployeeRepository)super.getRepository();
	}
}
