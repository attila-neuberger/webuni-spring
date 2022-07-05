package hu.webuni.hr.comtur.web;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.VacationRequestDto;
import hu.webuni.hr.comtur.dto.Views;
import hu.webuni.hr.comtur.mapper.VacationRequestMapper;
import hu.webuni.hr.comtur.model.VacationRequest;
import hu.webuni.hr.comtur.model.VacationRequestStatus;
import hu.webuni.hr.comtur.security.ExtendedUserPrincipal;
import hu.webuni.hr.comtur.service.VacationRequestService;
import hu.webuni.hr.comtur.service.exception.VacationRequestException;

/**
 * Vacation request controller. Methods use exception throwing.
 * 
 * @author comtur
 */
@RestController
@RequestMapping("/api/vacationrequests")
public class HrRestVacationRequestController {
	
	@Autowired
	VacationRequestService vacationRequestService;
	
	@Autowired
	VacationRequestMapper vacationRequestMapper;
	
	@PostMapping("/{requesterId}")
	@JsonView(Views.VisibleData.class)
	public VacationRequestDto create(@PathVariable long requesterId, @RequestBody @Valid VacationRequestDto vacationRequestDto) {
		checkAuthorization(requesterId);
		try {
			VacationRequest vacationRequest = vacationRequestService.createVacationRequest(requesterId,
					vacationRequestMapper.dtoToVacationRequest(vacationRequestDto));
			return vacationRequestMapper.vacationRequestToDto(vacationRequest);
		} catch (NoSuchElementException e) {
			throw new VacationRequestException(String.format("Employee with ID '%d' does not exist (requester employee).", requesterId));
		}
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
	
	@GetMapping("/{approverId}/approve/{id}")
	@JsonView(Views.VisibleData.class)
	public VacationRequestDto approve(@PathVariable long approverId, @PathVariable long id) {
		return changeStatus(approverId, id, VacationRequestStatus.APPROVED);
	}
	
	@GetMapping("/{approverId}/refuse/{id}")
	@JsonView(Views.VisibleData.class)
	public VacationRequestDto refuse(@PathVariable long approverId, @PathVariable long id) {
		return changeStatus(approverId, id, VacationRequestStatus.REFUSED);
	}
	
	private VacationRequestDto changeStatus(long approverId, long id, VacationRequestStatus status) {
		try {
			VacationRequest vacationRequest = vacationRequestService.changeVacationRequestStatus(approverId,
					id, status);
			return vacationRequestMapper.vacationRequestToDto(vacationRequest);
		} catch (NoSuchElementException | IllegalStateException e) {
			throw new VacationRequestException(e.getMessage());
		}
	}
	
	@PutMapping("/{requesterId}/modify/{id}")
	@JsonView(Views.VisibleData.class)
	public VacationRequestDto modify(@PathVariable long requesterId, @PathVariable long id,
			@RequestBody @Valid VacationRequestDto vacationRequestDto) {
		
		checkAuthorization(requesterId);
		try {
			VacationRequest vacationRequest = vacationRequestService.modifyVacationRequest(requesterId, id,
					vacationRequestMapper.dtoToVacationRequest(vacationRequestDto));
			return vacationRequestMapper.vacationRequestToDto(vacationRequest);
		} catch (NoSuchElementException | IllegalStateException e) {
			throw new VacationRequestException(e.getMessage());
		}
	}
	
	@DeleteMapping("/{requesterId}/delete/{id}")
	@JsonView(Views.VisibleData.class)
	public void delete(@PathVariable long requesterId, @PathVariable long id) {
		
		checkAuthorization(requesterId);
		try {
			vacationRequestService.deleteVacationRequest(requesterId, id);
		} catch (NoSuchElementException | IllegalStateException e) {
			throw new VacationRequestException(e.getMessage());
		}
	}
	
	@GetMapping
	@JsonView(Views.VisibleData.class)
	public Collection<VacationRequestDto> getAll(@PageableDefault(sort = {"id"}) Pageable pageable) {
		return vacationRequestMapper.vacationRequestsToDtos(vacationRequestService.pageAll(pageable).getContent());
	}
	
	@PostMapping("/find")
	@JsonView(Views.VisibleData.class)
	public Collection<VacationRequestDto> getRequestsByExample(
			@RequestBody VacationRequestDto vacationRequestDto,
			@RequestParam(value = "createdFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdFrom,
			@RequestParam(value = "createdTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdTo,
			@RequestParam(value = "requestedFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedFrom,
			@RequestParam(value = "requestedTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate requestedTo,
			@PageableDefault(sort = {"id"}) Pageable pageable) {
		List<VacationRequest> vacationRequests = vacationRequestService.pageAllByExample(
				vacationRequestMapper.dtoToVacationRequest(vacationRequestDto),
				pageable,
				new LocalDateTime[] {createdFrom, createdTo},
				new LocalDate[] {requestedFrom, requestedTo}
		).getContent();
		return vacationRequestMapper.vacationRequestsToDtos(vacationRequests);
	}
}
