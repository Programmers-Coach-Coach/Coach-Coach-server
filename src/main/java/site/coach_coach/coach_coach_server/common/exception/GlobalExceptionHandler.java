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
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;
import site.coach_coach.coach_coach_server.common.response.ErrorResponse;
import site.coach_coach.coach_coach_server.user.exception.IncorrectPasswordException;
import site.coach_coach.coach_coach_server.user.exception.InvalidUserException;
import site.coach_coach.coach_coach_server.user.exception.UserAlreadyExistException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
	public static final String DEFAULT_MESSAGE = ErrorMessage.INVALID_REQUEST;

	@ExceptionHandler(HandlerMethodValidationException.class)
	public ResponseEntity<ErrorResponse> handlerMethodValidationException(HandlerMethodValidationException ex) {
		Sentry.captureException(ex);
		String errorMessage = ex.getAllValidationResults()
			.getFirst()
			.getResolvableErrors()
			.getFirst()
			.getDefaultMessage();
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.BAD_REQUEST.value(), errorMessage);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(errorResponse);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		Sentry.captureException(ex);
		List<FieldError> fieldErrors = ex.getFieldErrors();
		String message = fieldErrors.stream()
			.map(FieldError::getDefaultMessage)
			.filter(Objects::nonNull)
			.findFirst()
			.orElse(DEFAULT_MESSAGE);

		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), message));
	}

	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
		Sentry.captureException(ex);
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
		log.error("Handled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(errorResponse);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<ErrorResponse> handleNoSuchElementException(NoSuchElementException ex) {
		Sentry.captureException(ex);
		ErrorResponse errorResponse = new ErrorResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());

		return ResponseEntity.status(HttpStatus.NOT_FOUND)
			.body(errorResponse);
	}

	@ExceptionHandler(AuthenticationException.class)
	public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex) {
		Sentry.captureException(ex);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ErrorMessage.NOT_FOUND_TOKEN));
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
		Sentry.captureException(ex);
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ErrorMessage.INVALID_VALUE));
	}

	@ExceptionHandler(UserAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> handleUserAlreadyExistException(UserAlreadyExistException ex) {
		Sentry.captureException(ex);
		log.error("Handled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.CONFLICT)
			.body(new ErrorResponse(HttpStatus.CONFLICT.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidInputException.class)
	public ResponseEntity<ErrorResponse> handleInvalidInputException(InvalidInputException ex) {
		Sentry.captureException(ex);
		log.error("Handled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidUserException.class)
	public ResponseEntity<ErrorResponse> handleInvalidUserException(InvalidUserException ex) {
		Sentry.captureException(ex);
		log.error("Handled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
			.body(new ErrorResponse(HttpStatus.UNAUTHORIZED.value(), ex.getMessage()));
	}

	@ExceptionHandler(InvalidFileException.class)
	public ResponseEntity<ErrorResponse> handleInvalidFileException(InvalidFileException ex) {
		log.error("Handled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(IncorrectPasswordException.class)
	public ResponseEntity<ErrorResponse> handleIncorrectPasswordException(IncorrectPasswordException ex) {
		Sentry.captureException(ex);
		log.error("Handled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
			.body(new ErrorResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleException(Exception ex) {
		Sentry.captureException(ex);
		log.error("Unhandled exception: [{}] - {}", ex.getClass().getSimpleName(), ex.getMessage());
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
			.body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ErrorMessage.SERVER_ERROR));
	}
}
