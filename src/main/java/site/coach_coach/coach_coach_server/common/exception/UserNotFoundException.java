package site.coach_coach.coach_coach_server.common.exception;

import java.util.NoSuchElementException;

public class UserNotFoundException extends NoSuchElementException {
	public UserNotFoundException(String message) {
		super(message);
	}
}
