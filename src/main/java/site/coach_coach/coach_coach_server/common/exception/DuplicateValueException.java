package site.coach_coach.coach_coach_server.common.exception;

import org.springframework.dao.DuplicateKeyException;

public class DuplicateValueException extends DuplicateKeyException {
	public DuplicateValueException(String message) {
		super(message);
	}
}
