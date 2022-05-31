package hu.webuni.hr.comtur.service.exception;

public class EmployeeException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public EmployeeException() {
		super();
	}

	public EmployeeException(String message, Throwable cause) {
		super(message, cause);
	}

	public EmployeeException(String message) {
		super(message);
	}

	public EmployeeException(Throwable cause) {
		super(cause);
	}
}
