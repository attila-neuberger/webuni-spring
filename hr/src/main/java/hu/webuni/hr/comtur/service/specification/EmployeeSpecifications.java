package hu.webuni.hr.comtur.service.specification;

import java.time.LocalDateTime;
import java.time.LocalTime;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.comtur.model.Company_;
import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.Employee_;
import hu.webuni.hr.comtur.model.Position_;

public class EmployeeSpecifications {

	public static Specification<Employee> hasId(long id) {
		// return (root, cq, cb) -> cb.equal(root.get("id"), id);
		
		Specification<Employee> employee = new Specification<>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Employee> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
				return cb.equal(root.get(Employee_.id), id);
			}
		};
		return employee;
	}
	
	public static Specification<Employee> hasName(String name) {
		return (root, cq, cb) -> cb.like(cb.upper(root.get(Employee_.name)), name.toUpperCase() + "%");
	}
	
	public static Specification<Employee> hasPositionName(String positionName) {
		return (root, cq, cb) -> cb.like(root.get(Employee_.position).get(Position_.name), positionName);
	}
	
	public static Specification<Employee> hasSalary(int salary) {
		return (root, cq, cb) -> cb.between(root.get(Employee_.salary), (int)(salary * 0.95), (int)(salary * 1.05));
	}
	
	public static Specification<Employee> hasStartDate(LocalDateTime startDate) {
		LocalDateTime startOfDay = LocalDateTime.of(startDate.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Employee_.startDate), startOfDay, startOfDay.plusDays(1));
	}
	
	public static Specification<Employee> hasCompanyName(String companyName) {
		return (root, cq, cb) -> cb.like(cb.upper(root.get(Employee_.company).get(Company_.name)), companyName.toUpperCase() + "%");
	}
}
