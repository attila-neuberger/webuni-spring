package hu.webuni.hr.comtur.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.VacationRequest;
import hu.webuni.hr.comtur.model.VacationRequestStatus;
import hu.webuni.hr.comtur.repository.VacationRequestRepository;
import hu.webuni.hr.comtur.service.specification.VacationRequestSpecifications;

@Service
public class VacationRequestService extends BaseService<VacationRequest> {
	
	/**
	 * Service for employees.
	 */
	@Autowired
	private EmployeeService employeeService;

	@Transactional
	public VacationRequest createVacationRequest(long requesterId, VacationRequest vacationRequest) throws NoSuchElementException {
		Optional<Employee> optionalRequester = employeeService.findById(requesterId);
		vacationRequest.setRequester(optionalRequester.get());
		return ((VacationRequestRepository)repository).save(vacationRequest);
	}
	
	@Transactional
	public VacationRequest changeVacationRequestStatus(long approverId, long id, VacationRequestStatus status)
			throws NoSuchElementException, IllegalStateException {
		Optional<Employee> optionalApprover = employeeService.findById(approverId);
		if (!optionalApprover.isPresent()) {
			throw new NoSuchElementException(String.format("Employee with ID '%d' does not exist (approver employee).", approverId));
		}
		Optional<VacationRequest> optionalVacationRequest = ((VacationRequestRepository)repository).findById(id);
		if (!optionalVacationRequest.isPresent()) {
			throw new NoSuchElementException(String.format("Vacation request with ID '%d' does not exist.", id));
		}
		VacationRequest vacationRequest = optionalVacationRequest.get();
		if (vacationRequest.getVacationRequestStatus() != VacationRequestStatus.CREATED) {
			throw new IllegalStateException(String.format("Vacation request with ID '%d' is already approved or refused.", id));
		}
		vacationRequest.setApprover(optionalApprover.get());
		vacationRequest.setVacationRequestStatus(status);
		return ((VacationRequestRepository)repository).save(vacationRequest);
	}
	
	@Transactional
	public VacationRequest modifyVacationRequest(long requesterId, long id, VacationRequest vacationRequest)
			throws NoSuchElementException, IllegalStateException {
		Optional<Employee> optionalRequester = employeeService.findById(requesterId);
		if (!optionalRequester.isPresent()) {
			throw new NoSuchElementException(String.format("Employee with ID '%d' does not exist (requester employee).", requesterId));
		}
		Optional<VacationRequest> optionalOriginalVacationRequest = ((VacationRequestRepository)repository).findById(id);
		if (!optionalOriginalVacationRequest.isPresent()) {
			throw new NoSuchElementException(String.format("Vacation request with ID '%d' does not exist.", id));
		}
		VacationRequest originalVacationRequest = optionalOriginalVacationRequest.get();
		if (originalVacationRequest.getRequester().getId() != requesterId) {
			throw new IllegalStateException(String.format("Vacation request with ID '%d' has different requester (original: %d, current: %d).",
					id, originalVacationRequest.getRequester().getId(), requesterId));
		}
		if (originalVacationRequest.getVacationRequestStatus() != VacationRequestStatus.CREATED) {
			throw new IllegalStateException(String.format("Vacation request with ID '%d' is already approved or refused.", id));
		}
		if (vacationRequest.getVacationStart() != null) {
			originalVacationRequest.setVacationStart(vacationRequest.getVacationStart());
		}
		if (vacationRequest.getVacationEnd() != null) {
			originalVacationRequest.setVacationEnd(vacationRequest.getVacationEnd());
		}
		return ((VacationRequestRepository)repository).save(originalVacationRequest);
	}
	
	@Transactional
	public void deleteVacationRequest(long requesterId, long id) throws NoSuchElementException, IllegalStateException {
		Optional<Employee> optionalRequester = employeeService.findById(requesterId);
		if (!optionalRequester.isPresent()) {
			throw new NoSuchElementException(String.format("Employee with ID '%d' does not exist (requester employee).", requesterId));
		}
		Optional<VacationRequest> optionalVacationRequest = ((VacationRequestRepository)repository).findById(id);
		if (!optionalVacationRequest.isPresent()) {
			throw new NoSuchElementException(String.format("Vacation request with ID '%d' does not exist.", id));
		}
		VacationRequest vacationRequest = optionalVacationRequest.get();
		if (vacationRequest.getVacationRequestStatus() != VacationRequestStatus.CREATED) {
			throw new IllegalStateException(String.format("Vacation request with ID '%d' is already approved or refused.", id));
		}
		if (vacationRequest.getRequester().getId() != requesterId) {
			throw new IllegalStateException(String.format("Vacation request with ID '%d' has different requester (original: %d, current: %d).",
					id, vacationRequest.getRequester().getId(), requesterId));
		}
		((VacationRequestRepository)repository).deleteById(id);
	}
	
	public Page<VacationRequest> pageAll(Pageable pageable) {
		return ((VacationRequestRepository)repository).findAll(pageable);
	}
	
	public Page<VacationRequest> pageAllByExample(VacationRequest example, Pageable pageable, LocalDateTime[] createdDates, LocalDate[] requestDates) {
		Specification<VacationRequest> specification = Specification.where(null);
		if (example.getVacationRequestStatus() != null) {
			specification = specification.and(VacationRequestSpecifications.hasStatus(example.getVacationRequestStatus()));
		}
		if (example.getRequester() != null && StringUtils.hasText(example.getRequester().getName())) {
			specification = specification.and(VacationRequestSpecifications.hasRequester(example.getRequester().getName()));
		}
		if (example.getApprover() != null && StringUtils.hasText(example.getApprover().getName())) {
			specification = specification.and(VacationRequestSpecifications.hasApprover(example.getApprover().getName()));
		}
		if (createdDates.length == 2 && createdDates[0] != null && createdDates[1] != null) {
			specification = specification.and(VacationRequestSpecifications.hasTsCreated(createdDates[0], createdDates[1]));
		}
		if (requestDates.length == 2 && requestDates[0] != null && requestDates[1] != null) {
			specification = specification.and(VacationRequestSpecifications.hasDate(requestDates[0], requestDates[1]));
		}
		return ((VacationRequestRepository)repository).findAll(specification, pageable);
	}
}
