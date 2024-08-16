package site.coach_coach.coach_coach_server.user.exception;

import org.springframework.security.core.AuthenticationException;

import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

public class InvalidUserException extends AuthenticationException {
	public InvalidUserException() {
		super(ErrorMessage.INVALID_USER);
	}
}
