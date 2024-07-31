package site.coach_coach.coach_coach_server.user.exception;

import org.springframework.dao.DuplicateKeyException;

public class UserAlreadyExistException extends DuplicateKeyException {

	public UserAlreadyExistException(String message) {
		super(message);
	}
}
