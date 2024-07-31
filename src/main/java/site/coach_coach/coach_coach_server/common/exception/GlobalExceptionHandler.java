package site.coach_coach.coach_coach_server.common.exception;

import java.util.List;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;

@RestControllerAdvice
public class GlobalExceptionHandler {
	public static final String DEFAULT_MESSAGE = ErrorMessage.INVALID_REQUEST;

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

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(ErrorMessage.INVALID_VALUE));
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

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(ErrorMessage.SERVER_ERROR));
	}
}
