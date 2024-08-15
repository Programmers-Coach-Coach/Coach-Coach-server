package site.coach_coach.coach_coach_server.user.exception;

import org.springframework.security.core.AuthenticationException;

import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

public class IncorrectPasswordException extends AuthenticationException {
	public IncorrectPasswordException() {
		super(ErrorMessage.INCORRECT_PASSWORD);
	}
}
