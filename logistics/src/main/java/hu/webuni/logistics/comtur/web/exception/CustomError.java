package hu.webuni.logistics.comtur.web.exception;

import java.util.List;

import org.springframework.validation.FieldError;

/**
 * Class for custom errors.
 * 
 * @author comtur
 */
public class CustomError {

	/**
	 * Error message.
	 */
	private String message;

	/**
	 * Error code.
	 */
	private int errorCode;

	/**
	 * List of error fields.
	 */
	private List<FieldError> fieldErrors;

	/**
	 * Creates custom error class.
	 * 
	 * @param message   Message of the error.
	 * @param errorCode Code of the error.
	 */
	public CustomError(String message, int errorCode) {
		this.message = message;
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public List<FieldError> getFieldErrors() {
		return fieldErrors;
	}

	public void setFieldErrors(List<FieldError> fieldErrors) {
		this.fieldErrors = fieldErrors;
	}
}
