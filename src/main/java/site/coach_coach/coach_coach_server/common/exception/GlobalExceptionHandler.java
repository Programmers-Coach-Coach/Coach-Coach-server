package site.coach_coach.coach_coach_server.common.exception;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import javax.naming.AuthenticationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import site.coach_coach.coach_coach_server.auth.exception.ExpiredTokenException;
import site.coach_coach.coach_coach_server.auth.exception.InvalidTokenException;
import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	public static final String DEFAULT_MESSAGE = ErrorMessage.INVALID_REQUEST.getMessage();

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		List<FieldError> fieldErrors = ex.getFieldErrors();
		String message = fieldErrors.stream()
			.map(FieldError::getDefaultMessage)
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(DEFAULT_MESSAGE);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(message));
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(errorResponse);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
		ErrorResponse errorResponse = new ErrorResponse(ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(errorResponse);
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(ErrorMessage.INVALID_VALUE.getMessage()));
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(InvalidTokenException.class)
	public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(ExpiredTokenException.class)
	public ResponseEntity<ErrorResponse> handleExpiredTokenException(ExpiredTokenException ex) {
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(ErrorMessage.SERVER_ERROR.getMessage()));
	}
}
