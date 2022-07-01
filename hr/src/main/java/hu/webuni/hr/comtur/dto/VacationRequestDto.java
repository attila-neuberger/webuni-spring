package hu.webuni.hr.comtur.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonView;

import hu.webuni.hr.comtur.dto.Views.VisibleData;
import hu.webuni.hr.comtur.model.VacationRequestStatus;

public class VacationRequestDto implements IDtoKey {

	@JsonView(VisibleData.class)
	private long id;
	
	@JsonView(VisibleData.class)
	private LocalDateTime tsCreated;
	
	@JsonView(VisibleData.class)
	private EmployeeDto requester;
	
	@JsonView(VisibleData.class)
	private EmployeeDto approver;
	
	@JsonView(VisibleData.class)
	@FutureOrPresent
	@NotNull
	private LocalDate start;
	
	@JsonView(VisibleData.class)
	@FutureOrPresent
	@NotNull
	private LocalDate end;
	
	@JsonView(VisibleData.class)
	@Column(nullable = false)
	private VacationRequestStatus vacationRequestStatus;
	
	public VacationRequestDto() {
		this.tsCreated = LocalDateTime.now();
		this.vacationRequestStatus = VacationRequestStatus.CREATED;
	}
	
	public VacationRequestDto(
			@NotNull @FutureOrPresent LocalDate vacationStart, 
			@NotNull @FutureOrPresent LocalDate vacationEnd) {
		this.tsCreated = LocalDateTime.now();
		this.start = vacationStart;
		this.end = vacationEnd;
		this.vacationRequestStatus = VacationRequestStatus.CREATED;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public LocalDateTime getTsCreated() {
		return tsCreated;
	}

	public void setTsCreated(LocalDateTime tsCreated) {
		this.tsCreated = tsCreated;
	}

	public EmployeeDto getRequester() {
		return requester;
	}

	public void setRequester(EmployeeDto requester) {
		this.requester = requester;
	}

	public EmployeeDto getApprover() {
		return approver;
	}

	public void setApprover(EmployeeDto approver) {
		this.approver = approver;
	}

	public LocalDate getStart() {
		return start;
	}

	public void setStart(LocalDate start) {
		this.start = start;
	}

	public LocalDate getEnd() {
		return end;
	}

	public void setEnd(LocalDate end) {
		this.end = end;
	}

	public VacationRequestStatus getVacationRequestStatus() {
		return vacationRequestStatus;
	}

	public void setVacationRequestStatus(VacationRequestStatus vacationRequestStatus) {
		this.vacationRequestStatus = vacationRequestStatus;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		VacationRequestDto other = (VacationRequestDto) obj;
		return id == other.id;
	}

	@Override
	public String toString() {
		return "VacationRequestDto [id=" + id + ", tsCreated=" + tsCreated + ", requester=" + requester + ", approver="
				+ approver + ", start=" + start + ", end=" + end
				+ ", vacationRequestStatus=" + vacationRequestStatus + "]";
	}
}
