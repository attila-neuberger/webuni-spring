package hu.webuni.logistics.comtur.web.exception;

/**
 * Exception for ID unique constraint, non existing ID, missing ID, etc.
 * 
 * @author comtur
 */
public class IdViolationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public IdViolationException(String message) {
		super(message);
	}

	public IdViolationException(String message, Throwable e) {
		super(message, e);
	}
}
