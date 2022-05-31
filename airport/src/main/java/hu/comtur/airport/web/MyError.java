package hu.comtur.airport.web;

import java.util.List;

import org.springframework.validation.FieldError;

public class MyError {

	private String message;
	private int errorCode;
	private List<FieldError> fieldErrors;
	
	public MyError(String message, int errorCode) {
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
