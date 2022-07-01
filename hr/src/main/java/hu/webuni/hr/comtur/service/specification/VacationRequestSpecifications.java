package hu.webuni.hr.comtur.service.specification;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import hu.webuni.hr.comtur.model.Employee_;
import hu.webuni.hr.comtur.model.VacationRequest;
import hu.webuni.hr.comtur.model.VacationRequestStatus;
import hu.webuni.hr.comtur.model.VacationRequest_;

public class VacationRequestSpecifications {

	public static Specification<VacationRequest> hasStatus(VacationRequestStatus status) {
		return (root, cq, cb) -> cb.equal(root.get(VacationRequest_.vacationRequestStatus), status);
	}
	
	public static Specification<VacationRequest> hasRequester(String employeeName) {
		return (root, cq, cb) -> cb.like(cb.upper(root.get(VacationRequest_.requester).get(Employee_.name)), employeeName.toUpperCase() + "%");
	}
	
	public static Specification<VacationRequest> hasApprover(String employeeName) {
		return (root, cq, cb) -> cb.like(cb.upper(root.get(VacationRequest_.approver).get(Employee_.name)), employeeName.toUpperCase() + "%");
	}
	
	public static Specification<VacationRequest> hasTsCreated(LocalDateTime from, LocalDateTime to) {
		return (root, cq, cb) -> cb.between(root.get(VacationRequest_.tsCreated), from, to);
	}
	
	public static Specification<VacationRequest> hasDate(LocalDate from, LocalDate to) {
		return (root, cq, cb) -> cb.not(cb.or(
				cb.lessThan(root.get(VacationRequest_.vacationEnd), from),
				cb.greaterThan(root.get(VacationRequest_.vacationStart), to)
			));
	}
}
