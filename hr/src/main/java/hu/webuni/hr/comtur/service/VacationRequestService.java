package hu.webuni.hr.comtur.service;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import hu.webuni.hr.comtur.model.Employee;
import hu.webuni.hr.comtur.model.VacationRequest;
import hu.webuni.hr.comtur.model.VacationRequestStatus;
import hu.webuni.hr.comtur.repository.VacationRequestRepository;
import hu.webuni.hr.comtur.security.ExtendedUserPrincipal;
import hu.webuni.hr.comtur.service.exception.VacationRequestException;
import hu.webuni.hr.comtur.service.specification.VacationRequestSpecifications;

@Service
public class VacationRequestService extends BaseService<VacationRequest> {
	
	/**
	 * Service for employees.
	 */
	@Autowired
	private EmployeeService employeeService;

	@Transactional
	public VacationRequest createVacationRequest(long requesterId, VacationRequest vacationRequest) throws NoSuchElementException,
			VacationRequestException {

		checkAuthorization(requesterId);
		Optional<Employee> optionalRequester = employeeService.findById(requesterId);
		vacationRequest.setRequester(optionalRequester.get());
		return ((VacationRequestRepository)repository).save(vacationRequest);
	}
	
	@Transactional
	public VacationRequest changeVacationRequestStatus(long approverId, long id, VacationRequestStatus status)
			throws NoSuchElementException, IllegalStateException {

		Optional<Employee> optionalApprover = employeeService.getEmployeeWithSubordinates(approverId);
		if (!optionalApprover.isPresent()) {
			throw new NoSuchElementException(String.format("Employee with ID '%d' does not exist (approver employee).", approverId));
		}
		Optional<VacationRequest> optionalVacationRequest = ((VacationRequestRepository)repository).findById(id);
		if (!optionalVacationRequest.isPresent()) {
			throw new NoSuchElementException(String.format("Vacation request with ID '%d' does not exist.", id));
		}
		Employee approver = optionalApprover.get();
		VacationRequest vacationRequest = optionalVacationRequest.get();
		if (vacationRequest.getVacationRequestStatus() != VacationRequestStatus.CREATED) {
			throw new IllegalStateException(String.format("Vacation request with ID '%d' is already approved or refused.", id));
		}
		boolean found = false;
		for (int i = 0; !found && i < approver.getSubordinates().size(); ++i) {
			if (approver.getSubordinates().get(i).getId() == vacationRequest.getRequester().getId()) {
				found = true;
			}
		}
		if (!found) {
			throw new IllegalStateException(String.format("Approver '%d' is not manager of '%d' (vacation request ID: '%d')",
					approverId, vacationRequest.getRequester().getId(), id));
		}
		
		vacationRequest.setApprover(approver);
		vacationRequest.setVacationRequestStatus(status);
		return ((VacationRequestRepository)repository).save(vacationRequest);
	}
	
	@Transactional
	public VacationRequest modifyVacationRequest(long requesterId, long id, VacationRequest vacationRequest)
			throws NoSuchElementException, IllegalStateException, VacationRequestException {

		checkAuthorization(requesterId);
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
	public void deleteVacationRequest(long requesterId, long id) throws NoSuchElementException, IllegalStateException, VacationRequestException {

		checkAuthorization(requesterId);
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
	
	public Page<VacationRequest> pageAllByExample(VacationRequest example, Pageable pageable, LocalDateTime[] createdDates) {
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
		if (example.getVacationStart() != null && example.getVacationEnd() != null) {
			specification = specification.and(VacationRequestSpecifications.hasDate(example.getVacationStart(), example.getVacationEnd()));
		}
		return ((VacationRequestRepository)repository).findAll(specification, pageable);
	}
	
	/**
	 * Checks user's authorization and requester ID.
	 * @param requesterId Requester ID path parameter.
	 * @throws VacationRequestException In case of authorization failure.
	 */
	private void checkAuthorization(long requesterId) throws VacationRequestException {
		ExtendedUserPrincipal principal = (ExtendedUserPrincipal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal.getEmployee().getId() != requesterId) {
			throw new VacationRequestException(String.format("Requester employee's ID (%d) is not the logged user's ID (%d)",
					requesterId, principal.getEmployee().getId()));
		}
	}
}
