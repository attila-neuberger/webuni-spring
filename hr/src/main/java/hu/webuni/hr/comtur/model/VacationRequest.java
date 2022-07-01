package hu.webuni.hr.comtur.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class VacationRequest extends Id {

	@Column(nullable = false)
	private LocalDateTime tsCreated;
	
	@ManyToOne
	@JoinColumn(name = "requester_id", nullable = false)
	private Employee requester;
	
	@ManyToOne
	@JoinColumn(name = "approver_id")
	private Employee approver;
	
	@Column(nullable = false)
	private LocalDate vacationStart;
	
	@Column(nullable = false)
	private LocalDate vacationEnd;
	
	@Column(nullable = false)
	private VacationRequestStatus vacationRequestStatus;

	public VacationRequest() {
		super();
		this.tsCreated = LocalDateTime.now();
		this.vacationRequestStatus = VacationRequestStatus.CREATED;
	}
	
	public VacationRequest(Employee requester, LocalDate vacationStart, LocalDate vacationEnd) {
		super();
		this.tsCreated = LocalDateTime.now();
		this.requester = requester;
		this.vacationStart = vacationStart;
		this.vacationEnd = vacationEnd;
		this.vacationRequestStatus = VacationRequestStatus.CREATED;
	}

	public LocalDateTime getTsCreated() {
		return tsCreated;
	}

	public void setTsCreated(LocalDateTime tsCreated) {
		this.tsCreated = tsCreated;
	}

	public Employee getRequester() {
		return requester;
	}

	public void setRequester(Employee requester) {
		this.requester = requester;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
	}

	public LocalDate getVacationStart() {
		return vacationStart;
	}

	public void setVacationStart(LocalDate vacationStart) {
		this.vacationStart = vacationStart;
	}

	public LocalDate getVacationEnd() {
		return vacationEnd;
	}

	public void setVacationEnd(LocalDate vacationEnd) {
		this.vacationEnd = vacationEnd;
	}

	public VacationRequestStatus getVacationRequestStatus() {
		return vacationRequestStatus;
	}

	public void setVacationRequestStatus(VacationRequestStatus vacationRequestStatus) {
		this.vacationRequestStatus = vacationRequestStatus;
	}
}
