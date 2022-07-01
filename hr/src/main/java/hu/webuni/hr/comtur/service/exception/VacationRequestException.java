package hu.webuni.hr.comtur.service.exception;

public class VacationRequestException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public VacationRequestException() {
		super();
	}

	public VacationRequestException(String message, Throwable cause) {
		super(message, cause);
	}

	public VacationRequestException(String message) {
		super(message);
	}

	public VacationRequestException(Throwable cause) {
		super(cause);
	}
}
