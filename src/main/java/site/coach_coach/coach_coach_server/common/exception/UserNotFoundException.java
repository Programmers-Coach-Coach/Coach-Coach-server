package site.coach_coach.coach_coach_server.common.exception;

import java.util.NoSuchElementException;

import site.coach_coach.coach_coach_server.common.constants.ErrorMessage;

public class UserNotFoundException extends NoSuchElementException {
	public UserNotFoundException() {
		super(ErrorMessage.NOT_FOUND_USER);
	}
}
