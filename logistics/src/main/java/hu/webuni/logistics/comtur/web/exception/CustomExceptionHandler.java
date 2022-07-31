package hu.webuni.logistics.comtur.web.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

/**
 * Exception handler class for controllers.
 * 
 * @author comtur
 */
@RestControllerAdvice
public class CustomExceptionHandler {

	/**
	 * Handles {@link IdViolationException}s.
	 * 
	 * @param e   Exception object.
	 * @param req Web request object.
	 * @return Response entity.
	 */
	@ExceptionHandler(IdViolationException.class)
	public ResponseEntity<CustomError> handleIdViolation(IdViolationException e, WebRequest req) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new CustomError(e.getMessage(), 1001));
	}
}
