package site.coach_coach.coach_coach_server.user.exception;

import javax.naming.AuthenticationException;

import site.coach_coach.coach_coach_server.common.validation.ErrorMessage;

public class IncorrectPasswordException extends AuthenticationException {
	public IncorrectPasswordException() {
		super(ErrorMessage.NOT_FOUND_USER.getMessage());
	}
}
