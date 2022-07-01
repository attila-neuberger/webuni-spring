package hu.webuni.hr.comtur.model;

public enum VacationRequestStatus {
	CREATED, // Requester created the request. Can be modified or deleted.
	APPROVED, // Approver approved the request.
	REFUSED // Approver refused the request.
}
